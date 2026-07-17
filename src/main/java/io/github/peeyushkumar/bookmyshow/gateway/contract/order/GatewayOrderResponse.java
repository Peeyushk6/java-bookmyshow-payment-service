package io.github.peeyushkumar.bookmyshow.gateway.contract.order;

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

    private String providerOrderId;

    private String checkoutUrl;

    private String gatewayMetadata;

    private LocalDateTime expiresAt;


}
