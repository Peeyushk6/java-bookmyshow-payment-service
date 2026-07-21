package io.github.peeyushkumar.bookmyshow.repository;

import io.github.peeyushkumar.bookmyshow.entity.Payment;
import io.github.peeyushkumar.bookmyshow.entity.PaymentAttempt;
import io.github.peeyushkumar.bookmyshow.enums.PaymentAttemptType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentAttemptRepository
        extends JpaRepository<PaymentAttempt, Long> {


    @Query("""
SELECT COALESCE(MAX(pa.attemptNumber),0)
FROM PaymentAttempt pa
WHERE pa.payment = :payment
AND pa.attemptType = :attemptType
""")
    Integer findMaxAttemptNumber(
            Payment payment,
            PaymentAttemptType attemptType
    );
    Optional<PaymentAttempt> findById(
            Long id
    );

}