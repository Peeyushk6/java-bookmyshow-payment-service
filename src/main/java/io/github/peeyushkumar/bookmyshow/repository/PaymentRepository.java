package io.github.peeyushkumar.bookmyshow.repository;

import io.github.peeyushkumar.bookmyshow.entity.Merchant;
import io.github.peeyushkumar.bookmyshow.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    Optional<Payment> findByProviderOrderId(String providerOrderId);

    Optional<Payment> findByProviderPaymentId(String providerPaymentId);

    Optional<Payment> findByCorrelationId(String correlationId);

    Optional<Payment> findByMerchantAndIdempotencyKey(
            Merchant merchant,
            String idempotencyKey
    );

}
