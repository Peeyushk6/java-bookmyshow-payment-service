package io.github.peeyushkumar.bookmyshow.entity;

import io.github.peeyushkumar.bookmyshow.enums.*;
import io.github.peeyushkumar.bookmyshow.gateway.contract.order.GatewayOrderResponse;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.context.annotation.Fallback;

import java.math.BigDecimal;
import java.sql.Ref;
import java.time.LocalDateTime;


@Entity
@Table(
        name = "payments",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_merchant_idempotency",
                        columnNames = {
                                "merchant_id" ,
                                "idempotency_key"
                        }
                ),
                @UniqueConstraint(
                        name = "uk_provider_payment",
                        columnNames = {
                                "provider",
                                "provider_payment_id"
                        }
                )
        }
)
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Payment extends BaseEntity{

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "merchant_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_payment_merchant")
    )
    private Merchant merchant;

    @Enumerated(EnumType.STRING)
    @Column()
    private PaymentMethod paymentMethod;

    @Column(nullable = false)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReferenceType referenceType;

    @Column(nullable = false)
    private String referenceId;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Currency currency;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentProvider provider;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentStatus status;

    @Column(unique = true)
    private String providerOrderId;

    @Column(unique = true)
    private String providerPaymentId;


    @Column(nullable = false)
    private String idempotencyKey;

    @Column(nullable = false)
    private String correlationId;

    @Column(name="expires_at")
    @Setter()
    private LocalDateTime expiresAt;

    private String checkoutUrl;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String gatewayMetadata;

    public void markProcessing(
            GatewayOrderResponse response) {

        this.providerOrderId = response.getProviderOrderId();

        this.checkoutUrl = response.getCheckoutUrl();

        this.gatewayMetadata = response.getGatewayMetadata();

        this.status = PaymentStatus.PROCESSING;

        this.expiresAt = response.getExpiresAt();

    }

}
