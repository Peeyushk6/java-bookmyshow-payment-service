package io.github.peeyushkumar.bookmyshow.factory;

import io.github.peeyushkumar.bookmyshow.dto.request.CreatePaymentRequest;
import io.github.peeyushkumar.bookmyshow.entity.Merchant;
import io.github.peeyushkumar.bookmyshow.entity.Payment;
import io.github.peeyushkumar.bookmyshow.enums.PaymentStatus;
import io.github.peeyushkumar.bookmyshow.util.CorrelationIdHolder;
import lombok.RequiredArgsConstructor;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class PaymentFactory {

    public Payment create(
            Merchant merchant,
            CreatePaymentRequest request
    ){

         return Payment.builder()
                .merchant(merchant)

                .description(request.getDescription())

                .referenceId(request.getReferenceId())

                .referenceType(request.getReferenceType())

                .amount(request.getAmount())

                .currency(request.getCurrency())

                .provider(request.getProvider())

                .paymentMethod(request.getPaymentMethod())

                .idempotencyKey(request.getIdempotencyKey())

                .status(PaymentStatus.INITIATED)

                .correlationId(CorrelationIdHolder.get())

                .build();

    }
}
