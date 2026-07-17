package io.github.peeyushkumar.bookmyshow.dto.response;

import io.github.peeyushkumar.bookmyshow.enums.Currency;
import io.github.peeyushkumar.bookmyshow.enums.PaymentMethod;
import io.github.peeyushkumar.bookmyshow.enums.PaymentProvider;
import io.github.peeyushkumar.bookmyshow.enums.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentResponse {

    private Long paymentId;

    private String providerOrderId;

    private PaymentStatus status;

    private PaymentProvider provider;

    private BigDecimal amount;

    private Currency currency;

    private PaymentMethod paymentMethod;

    private String checkoutUrl;

}
