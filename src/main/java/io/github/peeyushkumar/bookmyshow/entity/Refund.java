package io.github.peeyushkumar.bookmyshow.entity;

import io.github.peeyushkumar.bookmyshow.enums.PaymentProvider;
import io.github.peeyushkumar.bookmyshow.enums.RefundStatus;
import jakarta.persistence.*;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

@Entity
@Table(
        name = "refunds",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_provider_refund",
                        columnNames = {
                                "provider",
                                "provider_refund_id"
                        }
                )
        }
)
public class Refund extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "payment_id",
            foreignKey = @ForeignKey(name = "fk_refund_payment")
    )
    private Payment payment;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RefundStatus status;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentProvider provider;

    @Column(unique = true)
    private String providerRefundId;

    @Column(nullable = false)
    private String reason;

    @Column(nullable = false)
    private String correlationId;
}