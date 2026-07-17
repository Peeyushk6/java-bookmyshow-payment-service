package io.github.peeyushkumar.bookmyshow.gateway.dto.response;

import io.github.peeyushkumar.bookmyshow.enums.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GatewayPaymentResponse
{
    private String providerOrderId;

    private String checkoutUrl;

    private PaymentStatus status;

    private String rawResponse;
}
