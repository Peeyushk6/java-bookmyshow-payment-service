package io.github.peeyushkumar.bookmyshow.repository;

import io.github.peeyushkumar.bookmyshow.entity.Payment;
import io.github.peeyushkumar.bookmyshow.entity.PaymentAttempt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentAttemptRepository
        extends JpaRepository<PaymentAttempt, Long> {

    List<PaymentAttempt> findByPaymentOrderByAttemptNumberAsc(
            Payment payment
    );

    Optional<PaymentAttempt> findTopByPaymentOrderByAttemptNumberDesc(
            Payment payment
    );

}