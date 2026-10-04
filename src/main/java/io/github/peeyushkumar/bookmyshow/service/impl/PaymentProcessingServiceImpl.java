package io.github.peeyushkumar.bookmyshow.service.impl;

import io.github.peeyushkumar.bookmyshow.entity.Payment;
import io.github.peeyushkumar.bookmyshow.entity.PaymentAttempt;
import io.github.peeyushkumar.bookmyshow.enums.FailureReason;
import io.github.peeyushkumar.bookmyshow.enums.PaymentAttemptType;
import io.github.peeyushkumar.bookmyshow.enums.PaymentStatus;
import io.github.peeyushkumar.bookmyshow.exception.payment.InvalidPaymentStateException;
import io.github.peeyushkumar.bookmyshow.exception.paymentattempt.PaymentNotFoundException;
import io.github.peeyushkumar.bookmyshow.gateway.contract.order.GatewayCreateOrderRequest;
import io.github.peeyushkumar.bookmyshow.gateway.contract.order.GatewayOrderResponse;
import io.github.peeyushkumar.bookmyshow.gateway.router.PaymentGatewayRouter;
import io.github.peeyushkumar.bookmyshow.mapper.PaymentGatewayMapper;
import io.github.peeyushkumar.bookmyshow.service.PaymentAttemptService;
import io.github.peeyushkumar.bookmyshow.service.PaymentPersistenceService;
import io.github.peeyushkumar.bookmyshow.service.PaymentProcessingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class PaymentProcessingServiceImpl
        implements PaymentProcessingService {

    private final PaymentPersistenceService paymentPersistenceService;

    private final PaymentGatewayMapper paymentGatewayMapper;

    private final PaymentGatewayRouter paymentGatewayRouter;

    private final PaymentAttemptService paymentAttemptService;

    @Override
    public Payment processPayment(Long paymentId) {

        // 1. Load Payment
        Payment payment =
                paymentPersistenceService
                        .findById(paymentId)
                        .orElseThrow(() ->
                                new PaymentNotFoundException(paymentId)
                        );

        // 2. Already processed (idempotency)
        if (payment.getStatus() == PaymentStatus.PROCESSING) {
            return payment;
        }

        // 3. Validate state
        if (payment.getStatus() != PaymentStatus.INITIATED) {
            throw new InvalidPaymentStateException(paymentId);
        }



        // 4. Build gateway request
        GatewayCreateOrderRequest gatewayRequest =
                paymentGatewayMapper.toGatewayRequest(payment);

        // 5. Start audit attempt
        PaymentAttempt attempt =
                paymentAttemptService.startAttempt(
                        payment,
                        PaymentAttemptType.CREATE_ORDER,
                        gatewayRequest.toString()
                );

        Long attemptId = attempt.getId();

        try {

            // 6. Create gateway order
            GatewayOrderResponse gatewayResponse =
                    paymentGatewayRouter.createOrder(
                            payment.getProvider(),
                            gatewayRequest
                    );

            // 7. Update payment aggregate
            payment.markProcessing(
                    gatewayResponse.getProviderOrderId(),
                    gatewayResponse.getCheckoutUrl(),
                    gatewayResponse.getGatewayMetadata(),
                    gatewayResponse.getExpiresAt()
            );

            paymentPersistenceService.update(payment);

            // 8. Audit success
            recordAttemptSuccess(
                    attemptId,
                    gatewayResponse
            );

            return payment;

        }
        catch (Exception ex) {

            recordAttemptFailure(
                    attemptId,
                    FailureReason.GATEWAY_ERROR,
                    ex
            );

            throw ex;
        }
    }

    private void recordAttemptSuccess(
            Long attemptId,
            GatewayOrderResponse response
    ) {

        try {

            paymentAttemptService.markSuccess(
                    attemptId,
                    response.getPaymentStatus(),
                    response.getProviderOrderId(),
                    null,
                    response.getGatewayMetadata()
            );

        } catch (Exception ex) {

            log.error(
                    "Unable to mark payment attempt {} as SUCCESS",
                    attemptId,
                    ex
            );

        }

    }

    private void recordAttemptFailure(
            Long attemptId,
            FailureReason failureReason,
            Exception exception
    ) {

        try {

            paymentAttemptService.markFailure(
                    attemptId,
                    failureReason,
                    exception.getMessage()
            );

        } catch (Exception ex) {

            log.error(
                    "Unable to mark payment attempt {} as FAILED",
                    attemptId,
                    ex
            );

        }

    }

}
