package io.github.peeyushkumar.bookmyshow.dto.response;

import io.github.peeyushkumar.bookmyshow.enums.PaymentStatus;
import lombok.Builder;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;

@Builder
public record PaymentVerificationResponse(

        Long paymentId,

        PaymentStatus status,

        String providerOrderId,

        String providerPaymentId,

        LocalDateTime verifiedAt

) {
}