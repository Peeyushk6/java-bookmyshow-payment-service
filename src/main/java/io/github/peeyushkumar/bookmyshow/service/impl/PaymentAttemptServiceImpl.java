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
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

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
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void markSuccess(Long attemptId, GatewayPaymentStatus gatewayStatus, String providerOrderId, String paymentId, String responsePayload) {
        PaymentAttempt attempt =
                persistenceAttemptService.findById(attemptId);

        attempt.completeSuccessfully(
                gatewayStatus,
                providerOrderId,
                paymentId,
                responsePayload
        );

        persistenceAttemptService.save(attempt);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void markFailure(
            Long attemptId,
            FailureReason failureReason,
            String responsePayload
    ) {

        PaymentAttempt attempt =
                persistenceAttemptService.findById(attemptId);

        attempt.completeWithFailure(
                failureReason,
                responsePayload
        );

        persistenceAttemptService.save(attempt);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void markTimeout(Long attemptId) {

        PaymentAttempt attempt =
                persistenceAttemptService.findById(attemptId);

        attempt.completeWithTimeout();

        persistenceAttemptService.save(attempt);
    }
}
