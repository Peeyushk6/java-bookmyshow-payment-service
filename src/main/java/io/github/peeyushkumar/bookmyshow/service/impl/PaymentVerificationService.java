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
import io.github.peeyushkumar.bookmyshow.exception.payment.PaymentNotFoundException;
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
@Transactional
@Slf4j
public class PaymentVerificationService {

    private final PaymentPersistenceService paymentPersistenceService;

    private final PaymentGatewayRouter paymentGatewayRouter;

    private final PaymentGatewayMapper gatewayMapper;

    private final PaymentMapper paymentMapper;

    private final PaymentAttemptService paymentAttemptService;

    public PaymentVerificationResponse verify(
            PaymentVerificationRequest request
    ) {

        // 1. Load Payment
        Payment payment = loadPayment(request);

        // 2. Idempotency
        if (payment.getStatus() == PaymentStatus.SUCCESS) {
            return paymentMapper.toVerificationResponse(payment);
        }

        // 3. Validate Current State
        validatePaymentState(payment, request);

        // 4. Build Gateway Request
        GatewayVerifyPaymentRequest gatewayRequest =
                gatewayMapper.toGatewayVerifyPaymentRequest(request);

        // 5. Every gateway interaction MUST have an attempt
        PaymentAttempt attempt =
                paymentAttemptService.startAttempt(
                        payment,
                        PaymentAttemptType.VERIFY_PAYMENT,
                        gatewayRequest.toString()
                );

        try {

            // 6. Call Gateway
            GatewayVerifyPaymentResponse gatewayResponse =
                    paymentGatewayRouter.verifyPayment(
                            payment.getProvider(),
                            gatewayRequest
                    );

            // 7. Validate Signature
            validateGatewayResponse(gatewayResponse);

            // 8. Reconcile Response
            reconcilePayment(payment, gatewayResponse);

            // 9. Update Payment
            transitionPayment(payment, gatewayResponse);

            // 10. Persist Payment
            paymentPersistenceService.update(payment);

            // 11. Audit Success (Best effort)
            try {

                paymentAttemptService.markSuccess(
                        attempt,
                        gatewayResponse.getPaymentStatus(),
                        gatewayResponse.getProviderOrderId(),
                        gatewayResponse.getProviderPaymentId(),
                        gatewayResponse.getGatewayMetadata()
                );

            } catch (Exception ex) {

                log.error(
                        "Unable to mark payment attempt {} as SUCCESS",
                        attempt.getId(),
                        ex
                );

            }

            // 12. Response
            return paymentMapper.toVerificationResponse(payment);

        }
        catch (PaymentAttemptException ex) {

            try {

                paymentAttemptService.markFailure(
                        attempt,
                        ex.getFailureReason(),
                        ex.getMessage()
                );

            } catch (Exception logException) {

                log.error(
                        "Unable to mark payment attempt {} as FAILED",
                        attempt.getId(),
                        logException
                );

            }

            throw ex;

        }
        catch (PaymentServiceException ex) {

            throw ex;

        }
        catch (Exception ex) {

            try {

                paymentAttemptService.markFailure(
                        attempt,
                        FailureReason.INTERNAL_ERROR,
                        ex.getMessage()
                );

            } catch (Exception logException) {

                log.error(
                        "Unable to mark payment attempt {} as FAILED",
                        attempt.getId(),
                        logException
                );

            }

            throw ex;

        }

    }

    private Payment loadPayment(
            PaymentVerificationRequest request
    ) {

        return paymentPersistenceService
                .findByProviderOrderId(request.providerOrderId())
                .orElseThrow(() ->
                        new PaymentNotFoundException(
                                request.providerOrderId()
                        )
                );

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

        if (payment.getCurrency() != response.getCurrency()) {

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

}