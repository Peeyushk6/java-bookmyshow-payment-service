package io.github.peeyushkumar.bookmyshow.gateway.contract.payment;

import io.github.peeyushkumar.bookmyshow.enums.Currency;
import io.github.peeyushkumar.bookmyshow.enums.PaymentMethod;
import io.github.peeyushkumar.bookmyshow.gateway.contract.model.GatewayPaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GatewayPaymentDetails
{

        private String providerPaymentId;

        private String providerOrderId;

        private GatewayPaymentStatus status;

        private BigDecimal amount;

        private Currency currency;

        private PaymentMethod paymentMethod;

        /**
         * Redirect URL for Checkout
         */
        private String checkoutUrl;

        /**
         * Bank reference / UTR / PSP reference
         */
        private String transactionReference;

        /**
         * UTC Timestamp
         */
        private LocalDateTime paidAt;

        /**
         * Complete provider response as JSON
         */
        private String gatewayMetadata;

    }
