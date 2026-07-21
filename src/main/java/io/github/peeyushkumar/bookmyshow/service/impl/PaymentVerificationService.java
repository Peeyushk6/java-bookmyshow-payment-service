package io.github.peeyushkumar.bookmyshow.service.impl;

import io.github.peeyushkumar.bookmyshow.dto.request.PaymentVerificationRequest;
import io.github.peeyushkumar.bookmyshow.dto.response.PaymentVerificationResponse;
import io.github.peeyushkumar.bookmyshow.entity.Payment;
import io.github.peeyushkumar.bookmyshow.enums.PaymentStatus;
import io.github.peeyushkumar.bookmyshow.exception.payment.InvalidPaymentException;
import io.github.peeyushkumar.bookmyshow.exception.payment.PaymentNotFoundException;
import io.github.peeyushkumar.bookmyshow.gateway.contract.payment.GatewayVerifyPaymentRequest;
import io.github.peeyushkumar.bookmyshow.gateway.contract.payment.GatewayVerifyPaymentResponse;
import io.github.peeyushkumar.bookmyshow.gateway.router.PaymentGatewayRouter;
import io.github.peeyushkumar.bookmyshow.mapper.PaymentGatewayMapper;
import io.github.peeyushkumar.bookmyshow.mapper.PaymentMapper;
import io.github.peeyushkumar.bookmyshow.service.PaymentPersistenceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class PaymentVerificationService {

    private final PaymentPersistenceService paymentPersistenceService;

    private final PaymentGatewayRouter paymentGatewayRouter;

    private final PaymentGatewayMapper gatewayMapper;

    private final PaymentMapper paymentMapper;

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

        // 5. Verify with Gateway
        GatewayVerifyPaymentResponse gatewayResponse =
                paymentGatewayRouter.verifyPayment(
                        payment.getProvider(),
                        gatewayRequest
                );

        // 6. Validate Gateway Response
        validateGatewayResponse(gatewayResponse);

        // 7. Reconcile Gateway Response
        reconcilePayment(payment, gatewayResponse);

        // 8. Transition Payment
        transitionPayment(payment, gatewayResponse);

        // 9. Persist
        paymentPersistenceService.update(payment);

        // 10. Response
        return paymentMapper.toVerificationResponse(payment);

    }

    private Payment loadPayment(
            PaymentVerificationRequest request
    ) {

        return paymentPersistenceService
                .findByProviderOrderId(
                        request.providerOrderId()
                )
                .orElseThrow(
                        () -> new PaymentNotFoundException(
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