package io.github.peeyushkumar.bookmyshow.entity;

import io.github.peeyushkumar.bookmyshow.enums.FailureReason;
import io.github.peeyushkumar.bookmyshow.enums.PaymentAttemptStatus;
import io.github.peeyushkumar.bookmyshow.enums.PaymentProvider;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "payment_attempts",
        uniqueConstraints = {

                @UniqueConstraint(
                        name = "uk_payment_attempt",
                        columnNames = {
                                "payment_id",
                                "attempt_number"
                        }
                )
        }
)
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentAttempt extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "payment_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_attempt_payment")
    )
    private Payment payment;

    @Column(nullable = false)
    private Integer attemptNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentProvider provider;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentAttemptStatus status;

    private String providerOrderId;

    private String providerPaymentId;

    private String gatewayResponseCode;

    @Enumerated(EnumType.STRING)
    private FailureReason failureReason;

    @Lob
    private String requestPayload;

    @Lob
    private String responsePayload;

    @Column(nullable = false)
    private LocalDateTime startedAt;

    private LocalDateTime completedAt;
}