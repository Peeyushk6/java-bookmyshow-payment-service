package io.github.peeyushkumar.bookmyshow.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record PaymentVerificationRequest(

        @NotBlank
        String providerOrderId,

        @NotBlank
        String providerPaymentId,

        @NotBlank
        String providerSignature

) {

/*
 Why Record?

 - Immutable
 - Less boilerplate
 - Java 21 feature
 - Perfect for Request/Response DTOs

 NOTE:
 Do NOT use Records for JPA Entities.
 Hibernate requires mutable entities.
*/
}