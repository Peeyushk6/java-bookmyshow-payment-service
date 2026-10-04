package io.github.peeyushkumar.bookmyshow.service.impl;

import io.github.peeyushkumar.bookmyshow.dto.request.PaymentVerificationRequest;
import io.github.peeyushkumar.bookmyshow.dto.response.PaymentVerificationResponse;
import io.github.peeyushkumar.bookmyshow.entity.Payment;
import io.github.peeyushkumar.bookmyshow.entity.PaymentAttempt;
import io.github.peeyushkumar.bookmyshow.enums.FailureReason;
import io.github.peeyushkumar.bookmyshow.enums.PaymentAttemptType;
import io.github.peeyushkumar.bookmyshow.enums.PaymentStatus;
import io.github.peeyushkumar.bookmyshow.exception.base.PaymentAttemptException;
import io.github.peeyushkumar.bookmyshow.exception.base.PaymentServiceException;
import io.github.peeyushkumar.bookmyshow.exception.payment.InvalidPaymentException;
import io.github.peeyushkumar.bookmyshow.exception.payment.PaymentProviderNotFoundException;
import io.github.peeyushkumar.bookmyshow.gateway.contract.payment.GatewayVerifyPaymentRequest;
import io.github.peeyushkumar.bookmyshow.gateway.contract.payment.GatewayVerifyPaymentResponse;
import io.github.peeyushkumar.bookmyshow.gateway.router.PaymentGatewayRouter;
import io.github.peeyushkumar.bookmyshow.mapper.PaymentGatewayMapper;
import io.github.peeyushkumar.bookmyshow.mapper.PaymentMapper;
import io.github.peeyushkumar.bookmyshow.service.PaymentAttemptService;
import io.github.peeyushkumar.bookmyshow.service.PaymentPersistenceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class PaymentVerificationService {

    private final PaymentPersistenceService paymentPersistenceService;

    private final PaymentGatewayRouter paymentGatewayRouter;

    private final PaymentGatewayMapper paymentGatewayMapper;

    private final PaymentMapper paymentMapper;

    private final PaymentAttemptService paymentAttemptService;

    public PaymentVerificationResponse verify(
            PaymentVerificationRequest request
    ) {

        // 1. Load Payment
        Payment payment =
                paymentPersistenceService
                        .findByProviderOrderId(request.providerOrderId())
                        .orElseThrow(() ->
                                new PaymentProviderNotFoundException(
                                        request.providerPaymentId()
                                )
                        );

        // 2. Idempotency
        if (payment.getStatus() == PaymentStatus.SUCCESS) {
            return paymentMapper.toVerificationResponse(payment);
        }

        // 3. Validate Current State
        validatePaymentState(payment, request);

        // 4. Build Gateway Request
        GatewayVerifyPaymentRequest gatewayRequest =
                paymentGatewayMapper.toGatewayVerifyPaymentRequest(
                        request
                );

        // 5. Start Audit Attempt
        PaymentAttempt attempt =
                paymentAttemptService.startAttempt(
                        payment,
                        PaymentAttemptType.VERIFY_PAYMENT,
                        gatewayRequest.toString()
                );
        System.out.println("Attempt id = -------" + attempt.getId());


        Long attemptId = attempt.getId();
        log.info("Attempt created {}--------", attemptId);

        try {

            // 6. Verify Payment
            GatewayVerifyPaymentResponse gatewayResponse =
                    paymentGatewayRouter.verifyPayment(
                            payment.getProvider(),
                            gatewayRequest
                    );

            // 7. Validate Signature
            validateGatewayResponse(gatewayResponse);

            // 8. Reconcile Payment
            reconcilePayment(
                    payment,
                    gatewayResponse
            );

            // 9. Transition Aggregate
            transitionPayment(
                    payment,
                    gatewayResponse
            );

            // 10. Persist Payment
            paymentPersistenceService.update(payment);

            // 11. Complete Attempt
            completeAttemptSuccessfully(
                    attemptId,
                    gatewayResponse
            );

            return paymentMapper.toVerificationResponse(
                    payment
            );

        }
        catch (PaymentAttemptException ex) {

            completeAttemptWithFailure(
                    attemptId,
                    ex.getFailureReason(),
                    ex
            );

            throw ex;

        }
        catch (PaymentServiceException ex) {

            completeAttemptWithFailure(
                    attemptId,
                    FailureReason.UNKNOWN,
                    ex
            );

            throw ex;

        }
        catch (Exception ex) {

            completeAttemptWithFailure(
                    attemptId,
                    FailureReason.INTERNAL_ERROR,
                    ex
            );

            throw ex;

        }

    }

    private void validatePaymentState(
            Payment payment,
            PaymentVerificationRequest request
    ) {

        if (payment.getStatus() != PaymentStatus.PROCESSING) {

            throw new InvalidPaymentException(
                    request.providerOrderId()
            );

        }

    }

    private void validateGatewayResponse(
            GatewayVerifyPaymentResponse response
    ) {

        if (!response.isSignatureVerified()) {

            throw new InvalidPaymentException(
                    response.getProviderOrderId()
            );

        }

    }

    private void reconcilePayment(
            Payment payment,
            GatewayVerifyPaymentResponse response
    ) {

        if (!payment.getProviderOrderId().equals(
                response.getProviderOrderId()
        )) {

            throw new InvalidPaymentException(
                    payment.getProviderOrderId()
            );

        }

        if (payment.getAmount().compareTo(
                response.getAmount()
        ) != 0) {

            throw new InvalidPaymentException(
                    payment.getProviderOrderId()
            );

        }

        if (payment.getCurrency() !=
                response.getCurrency()) {

            throw new InvalidPaymentException(
                    payment.getProviderOrderId()
            );

        }

    }

    private void transitionPayment(
            Payment payment,
            GatewayVerifyPaymentResponse response
    ) {

        switch (response.getPaymentStatus()) {

            case CAPTURED ->
                    payment.markSuccess(
                            response.getProviderPaymentId(),
                            response.getPaymentMethod(),
                            response.getGatewayMetadata()
                    );

            case FAILED ->
                    payment.markFailure();

            case CREATED,
                 AUTHORIZED,
                 PENDING ->
                    payment.markVerificationPending();

            default ->
                    throw new InvalidPaymentException(
                            response.getProviderOrderId()
                    );

        }

    }

    private void completeAttemptSuccessfully(
            Long attemptId,
            GatewayVerifyPaymentResponse response
    ) {

        try {

            paymentAttemptService.markSuccess(
                    attemptId,
                    response.getPaymentStatus(),
                    response.getProviderOrderId(),
                    response.getProviderPaymentId(),
                    response.getGatewayMetadata()
            );

        } catch (Exception ex) {

            log.error(
                    "Failed to mark VERIFY_PAYMENT attempt {} as SUCCESS",
                    attemptId,
                    ex
            );

        }

    }

    private void completeAttemptWithFailure(
            Long attemptId,
            FailureReason failureReason,
            Exception exception
    ) {
        log.info("Recording failure for attempt {}", attemptId);


        try {
            paymentAttemptService.markFailure(
                    attemptId,
                    failureReason,
                    exception.getMessage()
            );

        } catch (Exception ex) {

            log.error(
                    "Failed to mark VERIFY_PAYMENT attempt {} as FAILED",
                    attemptId,
                    ex
            );

        }

    }

}