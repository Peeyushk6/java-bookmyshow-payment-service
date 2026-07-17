package io.github.peeyushkumar.bookmyshow.gateway.contract.order;

import io.github.peeyushkumar.bookmyshow.enums.Currency;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GatewayCreateOrderRequest {

    private String merchantId;

    private String orderReference;

    private BigDecimal amount;

    private Currency currency;

    private String description;

    private String callbackUrl;
}
