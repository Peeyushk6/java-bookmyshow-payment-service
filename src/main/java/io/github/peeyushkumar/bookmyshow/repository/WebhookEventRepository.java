package io.github.peeyushkumar.bookmyshow.repository;

import io.github.peeyushkumar.bookmyshow.entity.WebhookEvent;
import io.github.peeyushkumar.bookmyshow.enums.PaymentProvider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WebhookEventRepository
        extends JpaRepository<WebhookEvent, Long> {

    Optional<WebhookEvent> findByProviderAndProviderEventId(
            PaymentProvider provider,
            String providerEventId
    );

}
