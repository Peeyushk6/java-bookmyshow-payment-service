package io.github.peeyushkumar.bookmyshow.gateway.contract.refund;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GatewayRefundRequest {

    private String providerPaymentId;

    private BigDecimal amount;

    private String reason;
}
