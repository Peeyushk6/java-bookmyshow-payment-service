package io.github.peeyushkumar.bookmyshow.gateway.provider.razorpay.mapper;

import com.razorpay.Payment;
import io.github.peeyushkumar.bookmyshow.enums.Currency;
import io.github.peeyushkumar.bookmyshow.enums.PaymentMethod;
import io.github.peeyushkumar.bookmyshow.gateway.contract.model.GatewayPaymentStatus;
import io.github.peeyushkumar.bookmyshow.gateway.contract.payment.GatewayPaymentDetails;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Component
public class RazorpayPaymentDetailsMapper {
    private static final String ID = "id";

    private static final String STATUS = "status";

    private static final String METHOD = "method";

    private static final String ORDER_ID = "order_id";

    private static final String CURRENCY = "currency";


    public GatewayPaymentDetails toGatewayPaymentDetails(
            Payment payment
    ) {

        String providerPaymentId = getString(payment, ID);
        String providerOrderId = getString(payment, ORDER_ID);
        String status = getString(payment, STATUS);
        String currency = getString(payment, CURRENCY);
        String method = getString(payment, METHOD);

        return GatewayPaymentDetails.builder()
                .providerPaymentId(providerPaymentId)
                .providerOrderId(providerOrderId)
                .status(mapStatus(status))
                .amount(mapAmount(payment))
                .currency(mapCurrency(currency))
                .paymentMethod(mapPaymentMethod(method))
                .transactionReference(mapTransactionReference(payment))
                .gatewayMetadata(payment.toString())
                .paidAt(mapPaidAt(payment))
                .build();
    }

    private String getString(
            Payment payment,
            String key
    ) {

        Object value = payment.get(key);

        return value == null
                ? null
                : value.toString();

    }

    /**
     * Razorpay returns amount in the smallest currency unit.
     *
     * Example:
     * 50000 paise -> ₹500.00
     */
    private BigDecimal mapAmount(
            Payment payment
    ) {

        Object value = payment.get("amount");

        if (value == null) {
            throw new IllegalStateException(
                    "Amount missing from Razorpay payment."
            );
        }

        long amount = Long.parseLong(value.toString());

        return BigDecimal.valueOf(amount, 2);

    }

    private String mapTransactionReference(
            Payment payment
    ) {

        Object acquirerData = payment.get("acquirer_data");

        return acquirerData == null
                ? null
                : acquirerData.toString();

    }

    private LocalDateTime mapPaidAt(
            Payment payment
    ) {

        Object createdAt = payment.get("created_at");

        if (createdAt == null) {
            return null;
        }

        Instant instant = Instant.ofEpochSecond(
                Long.parseLong(createdAt.toString())
        );

        return LocalDateTime.ofInstant(
                instant,
                ZoneOffset.UTC
        );

    }

    private GatewayPaymentStatus mapStatus(
            String razorpayStatus
    ) {

        if (razorpayStatus == null) {
            return GatewayPaymentStatus.UNKNOWN;
        }

        return switch (razorpayStatus.toLowerCase()) {

            case "created" ->
                    GatewayPaymentStatus.CREATED;

            case "authorized" ->
                    GatewayPaymentStatus.AUTHORIZED;

            case "captured" ->
                    GatewayPaymentStatus.CAPTURED;

            case "failed" ->
                    GatewayPaymentStatus.FAILED;

            case "cancelled" ->
                    GatewayPaymentStatus.CANCELLED;

            case "refunded" ->
                    GatewayPaymentStatus.REFUNDED;

            default ->
                    GatewayPaymentStatus.UNKNOWN;
        };

    }

    private PaymentMethod mapPaymentMethod(
            String method
    ) {

        if (method == null) {
            return PaymentMethod.UNKNOWN;
        }

        return switch (method.toLowerCase()) {

            case "card" ->
                    PaymentMethod.CARD;

            case "upi" ->
                    PaymentMethod.UPI;

            case "wallet" ->
                    PaymentMethod.WALLET;

            case "netbanking" ->
                    PaymentMethod.NET_BANKING;

            case "emi" ->
                    PaymentMethod.EMI;

            default ->
                    PaymentMethod.UNKNOWN;

        };

    }

    private Currency mapCurrency(
            String currency
    ) {

        if (currency == null) {
            return null;
        }

        return Currency.valueOf(currency.toUpperCase());

    }

}