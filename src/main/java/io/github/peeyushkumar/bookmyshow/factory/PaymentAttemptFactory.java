package io.github.peeyushkumar.bookmyshow.factory;

import io.github.peeyushkumar.bookmyshow.entity.Payment;
import io.github.peeyushkumar.bookmyshow.entity.PaymentAttempt;
import io.github.peeyushkumar.bookmyshow.enums.PaymentAttemptType;
import org.springframework.stereotype.Component;

public interface PaymentAttemptFactory {
    PaymentAttempt create(
            Payment payment,

            PaymentAttemptType attemptType,

            Integer attemptNumner,

            String requestPayload
    );
}
