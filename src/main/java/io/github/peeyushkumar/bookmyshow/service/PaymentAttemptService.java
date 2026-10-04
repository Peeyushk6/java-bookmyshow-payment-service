package io.github.peeyushkumar.bookmyshow.service;

import io.github.peeyushkumar.bookmyshow.entity.Payment;
import io.github.peeyushkumar.bookmyshow.entity.PaymentAttempt;
import io.github.peeyushkumar.bookmyshow.enums.FailureReason;
import io.github.peeyushkumar.bookmyshow.enums.PaymentAttemptType;
import io.github.peeyushkumar.bookmyshow.gateway.contract.model.GatewayPaymentStatus;


public interface PaymentAttemptService {

    PaymentAttempt startAttempt(
            Payment payment,
            PaymentAttemptType attemptType,
            String requestPayload
    );

    void markSuccess(
            Long attemptId,
            GatewayPaymentStatus gatewayStatus,
            String providerOrderId,
            String providerPaymentId,
            String responsePayload
    );

    void markFailure(
            Long attemptId,
            FailureReason failureReason,
            String responsePayload
    );

    void markTimeout(
            Long attemptId
    );
}
