package io.github.peeyushkumar.bookmyshow.dto.request;

import io.github.peeyushkumar.bookmyshow.enums.Currency;
import io.github.peeyushkumar.bookmyshow.enums.PaymentMethod;
import io.github.peeyushkumar.bookmyshow.enums.PaymentProvider;
import io.github.peeyushkumar.bookmyshow.enums.ReferenceType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.math.BigDecimal;

/*@Data generates all of that automatically.
It includes:
@Getter
@Setter
@ToString
@EqualsAndHashCode
@RequiredArgsConstructor*/

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreatePaymentRequest {

    @NotBlank
    private String merchantId;

    @NotBlank
    private String referenceId;

    @NotNull
    private ReferenceType referenceType;

    @NotNull
    @Positive
    private BigDecimal amount;

    @NotNull
    private Currency currency;

    @NotNull
    private PaymentProvider provider;

    @NotNull
    private PaymentMethod paymentMethod;

    @NotBlank
    private String description;

    @NotBlank
    private String idempotencyKey;

}