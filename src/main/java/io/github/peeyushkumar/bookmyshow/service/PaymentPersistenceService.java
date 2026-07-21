package io.github.peeyushkumar.bookmyshow.service;

import io.github.peeyushkumar.bookmyshow.entity.Merchant;
import io.github.peeyushkumar.bookmyshow.entity.Payment;

import java.util.Optional;

public interface PaymentPersistenceService {

    Payment save(Payment payment);

    Payment update(Payment payment);

    Optional<Payment> findByMerchantAndIdempotencyKey(
            Merchant merchant,
            String idempotencyKey);

    Optional<Payment> findByProviderOrderId(
            String providerOrderId
    );

}
