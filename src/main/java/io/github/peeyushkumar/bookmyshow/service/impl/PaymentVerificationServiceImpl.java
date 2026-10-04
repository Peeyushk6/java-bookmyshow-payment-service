package io.github.peeyushkumar.bookmyshow.service.impl;

import io.github.peeyushkumar.bookmyshow.dto.request.PaymentVerificationRequest;
import io.github.peeyushkumar.bookmyshow.entity.Payment;
import io.github.peeyushkumar.bookmyshow.enums.PaymentStatus;
import io.github.peeyushkumar.bookmyshow.exception.payment.InvalidPaymentException;
import io.github.peeyushkumar.bookmyshow.exception.payment.PaymentProviderNotFoundException;
import io.github.peeyushkumar.bookmyshow.service.PaymentPersistenceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import io.github.peeyushkumar.bookmyshow.service.PaymentVerificationService;

@Service
@RequiredArgsConstructor
@Transactional
public class PaymentVerificationServiceImpl
        implements PaymentVerificationService {

    private final PaymentPersistenceService paymentPersistenceService;

    @Override
    public Long verify(
            PaymentVerificationRequest request
    ) {

        Payment payment =
                paymentPersistenceService
                        .findByProviderOrderId(
                                request.providerOrderId()
                        )
                        .orElseThrow(() ->
                                new PaymentProviderNotFoundException(
                                        request.providerPaymentId()
                                )
                        );

        if (payment.getStatus() == PaymentStatus.SUCCESS) {
            return payment.getId();
        }

        validatePaymentState(
                payment,
                request
        );

        return payment.getId();

    }

    private void validatePaymentState(
            Payment payment,
            PaymentVerificationRequest request
    ) {

        if (payment.getStatus() != PaymentStatus.PROCESSING) {

            throw new InvalidPaymentException(
                    request.providerOrderId()
            );

        }

    }

}