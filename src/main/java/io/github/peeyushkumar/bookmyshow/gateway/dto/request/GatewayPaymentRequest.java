package io.github.peeyushkumar.bookmyshow.gateway.dto.request;

import io.github.peeyushkumar.bookmyshow.enums.Currency;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GatewayPaymentRequest {

    private String paymentId;

    private BigDecimal amount;

    private Currency currency;

    private String description;

    private String referenceId;
}
