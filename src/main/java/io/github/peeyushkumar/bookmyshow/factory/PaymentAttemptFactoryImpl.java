package io.github.peeyushkumar.bookmyshow.factory;

import io.github.peeyushkumar.bookmyshow.entity.Payment;
import io.github.peeyushkumar.bookmyshow.entity.PaymentAttempt;
import io.github.peeyushkumar.bookmyshow.enums.PaymentAttemptStatus;
import io.github.peeyushkumar.bookmyshow.enums.PaymentAttemptType;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class PaymentAttemptFactoryImpl
implements PaymentAttemptFactory {
    @Override
    public PaymentAttempt create(Payment payment, PaymentAttemptType attemptType, Integer attemptNumner, String requestPayload) {

        return PaymentAttempt.builder()
                .payment(payment)
                .provider(
                        payment.getProvider()
                )
                .attemptType(
                        attemptType
                )
                .attemptNumber(
                        attemptNumner
                )
                .status(
                        PaymentAttemptStatus.STARTED
                )
                .startedAt(
                        LocalDateTime.now()
                )
                .build();
    }
}
