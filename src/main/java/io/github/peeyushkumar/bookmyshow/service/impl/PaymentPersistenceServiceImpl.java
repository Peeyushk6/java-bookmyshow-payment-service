package io.github.peeyushkumar.bookmyshow.service.impl;

import io.github.peeyushkumar.bookmyshow.entity.Merchant;
import io.github.peeyushkumar.bookmyshow.entity.Payment;
import io.github.peeyushkumar.bookmyshow.repository.PaymentRepository;
import io.github.peeyushkumar.bookmyshow.service.PaymentPersistenceService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@AllArgsConstructor
@Service
public class PaymentPersistenceServiceImpl implements PaymentPersistenceService
{
    private final PaymentRepository paymentRepository;

    @Override
    public Payment save(Payment payment) {
        System.out.println(payment.getCreatedAt());
        return paymentRepository.save(payment);
    }


    @Override
    public Payment update(Payment payment) {
        return paymentRepository.save(payment);
    }

    @Override
    public Optional<Payment> findByMerchantAndIdempotencyKey(Merchant merchant, String idempotencyKey) {

        return paymentRepository
                .findByMerchantAndIdempotencyKey(
                        merchant,
                        idempotencyKey
                );
    }

}
