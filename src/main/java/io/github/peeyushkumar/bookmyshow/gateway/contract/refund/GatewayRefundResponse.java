package io.github.peeyushkumar.bookmyshow.gateway.contract.refund;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GatewayRefundResponse {

    private boolean success;

    private String providerRefundId;

    private String message;
}
