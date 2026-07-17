package io.github.peeyushkumar.bookmyshow.gateway.contract.payment;

import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GatewayVerifyPaymentRequest
{

    private String providerOrderId;

    private String providerPaymentId;

    private String gatewaySignature;
}
