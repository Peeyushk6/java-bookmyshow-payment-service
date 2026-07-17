package io.github.peeyushkumar.bookmyshow.gateway.contract.payment;

import io.github.peeyushkumar.bookmyshow.gateway.contract.model.GatewayPaymentStatus;
import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GatewayVerifyPaymentResponse {

    private boolean signatureVerified;

    private GatewayPaymentStatus paymentStatus;

    private String providerPaymentId;

    private String providerOrderId;

    private String message;

}
