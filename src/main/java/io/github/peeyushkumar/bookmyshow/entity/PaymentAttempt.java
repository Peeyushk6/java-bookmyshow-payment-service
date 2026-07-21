package io.github.peeyushkumar.bookmyshow.entity;

import io.github.peeyushkumar.bookmyshow.enums.*;
import io.github.peeyushkumar.bookmyshow.gateway.contract.model.GatewayPaymentStatus;
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
                                "attempt_number",
                                "attempt_type"
                        }
                )
        }
)
@Getter
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
    private PaymentAttemptType attemptType;

    /**
     * Whether the interaction with the provider
     * completed successfully.
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentAttemptStatus status;

    /**
     * Status returned by the payment provider.
     */
    @Enumerated(EnumType.STRING)
    private GatewayPaymentStatus gatewayPaymentStatus;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentProvider provider;

    private String providerOrderId;

    private String providerPaymentId;

    @Enumerated(EnumType.STRING)
    private FailureReason failureReason;

    @Lob
    @Column(columnDefinition = "JSON")
    private String requestPayload;

    @Lob
    @Column(columnDefinition = "JSON")
    private String responsePayload;

    private LocalDateTime startedAt;

    private LocalDateTime completedAt;

    private String providerResponseCode;

    public void completeSuccessfully(
            GatewayPaymentStatus gatewayPaymentStatus,
            String providerOrderId,
            String providerPaymentId,
            String responsePayload
    ) {

        this.status = PaymentAttemptStatus.SUCCESS;
        this.gatewayPaymentStatus = gatewayPaymentStatus;
        this.providerOrderId = providerOrderId;
        this.providerPaymentId = providerPaymentId;
        this.responsePayload = responsePayload;
        this.completedAt = LocalDateTime.now();

    }

    public void completeWithFailure(
            FailureReason failureReason,
            String responsePayload
    ) {

        this.status = PaymentAttemptStatus.FAILED;
        this.failureReason = failureReason;
        this.responsePayload = responsePayload;
        this.completedAt = LocalDateTime.now();

    }

    public void completeWithTimeout() {

        this.status = PaymentAttemptStatus.TIMEOUT;
        this.completedAt = LocalDateTime.now();

    }

    public boolean isCompleted() {

        return completedAt != null;

    }

    public boolean isSuccessful() {

        return status == PaymentAttemptStatus.SUCCESS;

    }
}