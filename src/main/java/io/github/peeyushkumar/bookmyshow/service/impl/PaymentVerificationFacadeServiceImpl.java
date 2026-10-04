package io.github.peeyushkumar.bookmyshow.service.impl;

import io.github.peeyushkumar.bookmyshow.dto.request.PaymentVerificationRequest;
import io.github.peeyushkumar.bookmyshow.dto.response.PaymentVerificationResponse;
import io.github.peeyushkumar.bookmyshow.service.PaymentVerificationFacadeService;
import io.github.peeyushkumar.bookmyshow.service.PaymentVerificationProcessingService;
import io.github.peeyushkumar.bookmyshow.service.PaymentVerificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Orchestrates the complete payment verification workflow.
 *
 * Why does this class exist?
 *
 * Verification is intentionally split into two transactions.
 *
 * Transaction 1
 * -------------------------
 * - Load Payment
 * - Validate current state
 * - Commit
 *
 * Transaction 2
 * -------------------------
 * - Verify with Gateway
 * - Record PaymentAttempt
 * - Update Payment
 *
 * This avoids long-running transactions while waiting on external APIs
 * and prevents transaction visibility issues with PaymentAttempt auditing.
 */
@Service
@RequiredArgsConstructor
public class PaymentVerificationFacadeServiceImpl
        implements PaymentVerificationFacadeService {

    private final PaymentVerificationService paymentVerificationService;

    private final PaymentVerificationProcessingService
            paymentVerificationProcessingService;

    @Override
    public PaymentVerificationResponse verify(
            PaymentVerificationRequest request
    ) {

        Long paymentId =
                paymentVerificationService.verify(
                        request
                );

        return paymentVerificationProcessingService.process(
                paymentId,
                request
        );

    }

}