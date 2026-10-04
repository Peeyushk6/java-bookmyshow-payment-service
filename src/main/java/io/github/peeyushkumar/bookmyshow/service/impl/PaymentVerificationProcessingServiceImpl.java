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
import io.github.peeyushkumar.bookmyshow.exception.paymentattempt.PaymentNotFoundException;
import io.github.peeyushkumar.bookmyshow.gateway.contract.payment.GatewayVerifyPaymentRequest;
import io.github.peeyushkumar.bookmyshow.gateway.contract.payment.GatewayVerifyPaymentResponse;
import io.github.peeyushkumar.bookmyshow.gateway.router.PaymentGatewayRouter;
import io.github.peeyushkumar.bookmyshow.mapper.PaymentGatewayMapper;
import io.github.peeyushkumar.bookmyshow.mapper.PaymentMapper;
import io.github.peeyushkumar.bookmyshow.service.PaymentAttemptService;
import io.github.peeyushkumar.bookmyshow.service.PaymentPersistenceService;
import io.github.peeyushkumar.bookmyshow.service.PaymentVerificationProcessingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class PaymentVerificationProcessingServiceImpl
        implements PaymentVerificationProcessingService {

    private final PaymentPersistenceService paymentPersistenceService;

    private final PaymentGatewayRouter paymentGatewayRouter;

    private final PaymentGatewayMapper paymentGatewayMapper;

    private final PaymentMapper paymentMapper;

    private final PaymentAttemptService paymentAttemptService;

    @Override
    public PaymentVerificationResponse process(
            Long paymentId,
            PaymentVerificationRequest request
    ) {

        Payment payment =
                paymentPersistenceService
                        .findById(paymentId)
                        .orElseThrow(() ->
                                new PaymentNotFoundException(paymentId)
                        );

        if (payment.getStatus() == PaymentStatus.SUCCESS) {
            return paymentMapper.toVerificationResponse(payment);
        }

        GatewayVerifyPaymentRequest gatewayRequest =
                paymentGatewayMapper
                        .toGatewayVerifyPaymentRequest(request);

        PaymentAttempt attempt =
                paymentAttemptService.startAttempt(
                        payment,
                        PaymentAttemptType.VERIFY_PAYMENT,
                        gatewayRequest.toString()
                );

        Long attemptId = attempt.getId();

        try {

            GatewayVerifyPaymentResponse gatewayResponse =
                    paymentGatewayRouter.verifyPayment(
                            payment.getProvider(),
                            gatewayRequest
                    );

            validateGatewayResponse(gatewayResponse);

            reconcilePayment(
                    payment,
                    gatewayResponse
            );

            transitionPayment(
                    payment,
                    gatewayResponse
            );

            paymentPersistenceService.update(payment);

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

    private void validateGatewayResponse(
            GatewayVerifyPaymentResponse response
    ) {

        if (!response.isSignatureVerified()) {

            throw new InvalidPaymentException(
                    "Payment signature verification failed for ProviderOrderId : " + response.getProviderOrderId()
            );

        }

    }

    private void reconcilePayment(
            Payment payment,
            GatewayVerifyPaymentResponse response
    ) {

        if (!payment.getProviderOrderId()
                .equals(response.getProviderOrderId())) {

            throw new InvalidPaymentException(
                    "Provider order id mismatch for ProviderOrderId : " + payment.getProviderOrderId()
            );

        }

        if (payment.getAmount()
                .compareTo(response.getAmount()) != 0) {

            throw new InvalidPaymentException(
                    "Payment amount mismatch for ProviderOrderId : " + payment.getProviderOrderId()
            );

        }

        if (payment.getCurrency() != response.getCurrency()) {

            throw new InvalidPaymentException(
                    "Payment currency mismatch for ProviderOrderId: " + payment.getProviderOrderId()
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
                            "Invalid payment status for ProviderOrderId : " + response.getProviderOrderId()
                    );

        }

    }

    private void completeAttemptSuccessfully(
            Long attemptId,
            GatewayVerifyPaymentResponse response
    ) {

        try {
            log.info("Updating attempt {}", attemptId);


            paymentAttemptService.markSuccess(
                    attemptId,
                    response.getPaymentStatus(),
                    response.getProviderOrderId(),
                    response.getProviderPaymentId(),
                    response.getGatewayMetadata()
            );

            log.info("Attempt updated");

        }
        catch (Exception ex) {

            log.error(
                    "Unable to mark verification attempt {} as SUCCESS",
                    attemptId,
                    ex
            );

        }

    }

    private void completeAttemptWithFailure(
            Long attemptId,
            FailureReason reason,
            Exception exception
    ) {

        try {

            paymentAttemptService.markFailure(
                    attemptId,
                    reason,
                    exception.getMessage()
            );

        }
        catch (Exception ex) {

            log.error(
                    "Unable to mark verification attempt {} as FAILED",
                    attemptId,
                    ex
            );

        }

    }

}