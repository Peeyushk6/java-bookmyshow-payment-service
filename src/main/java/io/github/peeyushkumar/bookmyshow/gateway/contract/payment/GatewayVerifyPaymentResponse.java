package io.github.peeyushkumar.bookmyshow.gateway.contract.payment;

import io.github.peeyushkumar.bookmyshow.enums.Currency;
import io.github.peeyushkumar.bookmyshow.enums.PaymentMethod;
import io.github.peeyushkumar.bookmyshow.gateway.contract.model.GatewayPaymentStatus;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GatewayVerifyPaymentResponse {

    private boolean signatureVerified;

    private GatewayPaymentStatus paymentStatus;

    private String providerPaymentId;

    private String providerOrderId;

    private PaymentMethod paymentMethod;

    private Currency currency;

    private BigDecimal amount;

    private String gatewayMetadata;

    private String message;

}
