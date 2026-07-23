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
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;


public interface PaymentAttemptService {

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    PaymentAttempt startAttempt(
            Payment payment,
            PaymentAttemptType attemptType,
            String requestPayload
    );

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    void markSuccess(
            PaymentAttempt attempt,
            GatewayPaymentStatus gatewayStatus,
            String providerOrderId,
            String paymentId,
            String responsePayload
    );

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    void markFailure(
            PaymentAttempt attempt,

            FailureReason failureReason,

            String responsePayload
    );

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    void markTimeout(
            PaymentAttempt attempt
    );

}
