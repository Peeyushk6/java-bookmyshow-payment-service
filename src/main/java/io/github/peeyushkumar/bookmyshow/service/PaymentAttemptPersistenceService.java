package io.github.peeyushkumar.bookmyshow.service;

import io.github.peeyushkumar.bookmyshow.entity.Payment;
import io.github.peeyushkumar.bookmyshow.entity.PaymentAttempt;
import io.github.peeyushkumar.bookmyshow.enums.PaymentAttemptType;

public interface PaymentAttemptPersistenceService {

    PaymentAttempt save(
            PaymentAttempt attempt
    );

    PaymentAttempt update(
            PaymentAttempt attempt
    );

    PaymentAttempt findById(
            Long id
    );

    Integer nextAttemptNumber(
            Payment payment,
            PaymentAttemptType type
    );

}