package io.github.peeyushkumar.bookmyshow.service;

import io.github.peeyushkumar.bookmyshow.entity.Payment;
import io.github.peeyushkumar.bookmyshow.entity.PaymentAttempt;
import io.github.peeyushkumar.bookmyshow.enums.FailureReason;
import io.github.peeyushkumar.bookmyshow.enums.PaymentAttemptStatus;
import io.github.peeyushkumar.bookmyshow.enums.PaymentAttemptType;
import io.github.peeyushkumar.bookmyshow.exception.base.PaymentServiceException;
import io.github.peeyushkumar.bookmyshow.gateway.contract.model.GatewayPaymentStatus;
import io.github.peeyushkumar.bookmyshow.gateway.contract.order.GatewayCreateOrderRequest;
import io.github.peeyushkumar.bookmyshow.gateway.contract.order.GatewayOrderResponse;
import io.github.peeyushkumar.bookmyshow.gateway.contract.payment.GatewayVerifyPaymentRequest;
import io.github.peeyushkumar.bookmyshow.gateway.contract.payment.GatewayVerifyPaymentResponse;

import java.time.Instant;


public interface PaymentAttemptService {

    PaymentAttempt startAttempt(
            Payment payment,
            PaymentAttemptType attemptType,
            String requestPayload
    );

    void markSuccess(
            PaymentAttempt attempt,
            GatewayPaymentStatus gatewayStatus,
            String providerOrderId,
            String responsePayload
    );

    void markFailure(
            PaymentAttempt attempt,

            FailureReason failureReason,

            String responsePayload
    );

    void markTimeout(
            PaymentAttempt attempt
    );

}
