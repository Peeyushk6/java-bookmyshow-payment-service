package io.github.peeyushkumar.bookmyshow.service.impl;

import io.github.peeyushkumar.bookmyshow.dto.request.CreatePaymentRequest;
import io.github.peeyushkumar.bookmyshow.entity.Merchant;
import io.github.peeyushkumar.bookmyshow.entity.Payment;
import io.github.peeyushkumar.bookmyshow.factory.PaymentFactory;
import io.github.peeyushkumar.bookmyshow.service.MerchantService;
import io.github.peeyushkumar.bookmyshow.service.PaymentPersistenceService;
import io.github.peeyushkumar.bookmyshow.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class PaymentServiceImpl implements PaymentService {

    private final MerchantService merchantService;
    private final PaymentPersistenceService paymentPersistenceService;
    private final PaymentFactory paymentFactory;

    @Override
    public Long createPayment(
            CreatePaymentRequest request
    ) {

        Merchant merchant =
                merchantService.getActiveMerchant(
                        request.getMerchantId()
                );

        Payment existing =
                paymentPersistenceService
                        .findByMerchantAndIdempotencyKey(
                                merchant,
                                request.getIdempotencyKey()
                        )
                        .orElse(null);

        if (existing != null) {
            return existing.getId();
        }

        Payment payment =
                paymentFactory.create(
                        merchant,
                        request
                );

        payment =
                paymentPersistenceService.save(payment);

        return payment.getId();

    }
}