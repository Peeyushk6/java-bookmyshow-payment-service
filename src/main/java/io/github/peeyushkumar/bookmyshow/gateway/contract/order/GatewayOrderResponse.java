package io.github.peeyushkumar.bookmyshow.gateway.contract.order;

import io.github.peeyushkumar.bookmyshow.gateway.contract.model.GatewayPaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GatewayOrderResponse {

    /**
     * Gateway generated order id.
     */
    private String providerOrderId;

    /**
     * Initial order status returned by gateway.
     */
    private GatewayPaymentStatus paymentStatus;

    /**
     * Hosted checkout URL.
     * Null for providers that don't use hosted checkout.
     */
    private String checkoutUrl;

    /**
     * Complete provider response for auditing/debugging.
     */
    private String gatewayMetadata;

    /**
     * Order expiry as reported by the provider.
     */
    private LocalDateTime expiresAt;

}
