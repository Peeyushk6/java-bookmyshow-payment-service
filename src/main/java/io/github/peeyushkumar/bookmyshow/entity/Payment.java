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


//Rich Domain Models
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

    /**
     * Optional checkout URL.
     *
     * Some providers (Stripe Checkout) return a hosted payment page.
     * Others (Razorpay Orders) expect the frontend SDK to render checkout.
     */
    private String checkoutUrl;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String gatewayMetadata;

    public void markProcessing(
            String providerOrderId,
            String checkoutUrl,
            String gatewayMetadata,
            LocalDateTime expiresAt
    ){

        this.providerOrderId = providerOrderId;
        this.checkoutUrl = checkoutUrl;
        this.gatewayMetadata = gatewayMetadata;
        this.status = PaymentStatus.PROCESSING;
        this.expiresAt = expiresAt;

    }

    public void markSuccess(
            String providerPaymentId,
            PaymentMethod paymentMethod,
            String gatewayMetadata
    ){

        this.providerPaymentId = providerPaymentId;
        this.paymentMethod = paymentMethod;
        this.gatewayMetadata = gatewayMetadata;
        this.status = PaymentStatus.SUCCESS;

    }
    public void markFailure(){
        this.status = PaymentStatus.FAILED;
    }

    public void markVerificationPending() {
        this.status = PaymentStatus.PROCESSING;
    }

}
