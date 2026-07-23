package io.github.peeyushkumar.bookmyshow.service.impl;

import io.github.peeyushkumar.bookmyshow.entity.Payment;
import io.github.peeyushkumar.bookmyshow.entity.PaymentAttempt;
import io.github.peeyushkumar.bookmyshow.enums.FailureReason;
import io.github.peeyushkumar.bookmyshow.enums.PaymentAttemptType;
import io.github.peeyushkumar.bookmyshow.factory.PaymentAttemptFactory;
import io.github.peeyushkumar.bookmyshow.gateway.contract.model.GatewayPaymentStatus;
import io.github.peeyushkumar.bookmyshow.service.PaymentAttemptPersistenceService;
import io.github.peeyushkumar.bookmyshow.service.PaymentAttemptService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class PaymentAttemptServiceImpl
        implements PaymentAttemptService {

    private final PaymentAttemptPersistenceService persistenceAttemptService;

    private final PaymentAttemptFactory factory;

    @Override
    public PaymentAttempt startAttempt(Payment payment, PaymentAttemptType attemptType, String requestPayload) {
        Integer attemptNumber =
                persistenceAttemptService.nextAttemptNumber(
                        payment,
                        attemptType
                );

        PaymentAttempt attempt =
                factory.create(
                        payment,
                        attemptType,
                        attemptNumber,
                        requestPayload
                );

        return persistenceAttemptService.save(
                attempt
        );
    }

    @Override
    public void markSuccess(PaymentAttempt attempt, GatewayPaymentStatus gatewayStatus, String providerOrderId, String paymentId, String responsePayload) {
         attempt.completeSuccessfully(
                gatewayStatus,
                providerOrderId,
                attempt.getProviderPaymentId(),
                responsePayload
        );

        persistenceAttemptService.save(attempt);
    }

    @Override
    public void markFailure(PaymentAttempt attempt, FailureReason failureReason, String responsePayload) {
        attempt.completeWithFailure(
                failureReason,
                responsePayload
        );

        persistenceAttemptService.save(attempt);
    }

    @Override
    public void markTimeout(PaymentAttempt attempt) {

        attempt.completeWithTimeout();
        persistenceAttemptService.save(attempt);
    }
}
