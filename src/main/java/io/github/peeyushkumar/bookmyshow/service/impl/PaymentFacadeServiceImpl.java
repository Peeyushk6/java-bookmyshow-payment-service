package io.github.peeyushkumar.bookmyshow.service.impl;

import io.github.peeyushkumar.bookmyshow.dto.request.CreatePaymentRequest;
import io.github.peeyushkumar.bookmyshow.dto.response.PaymentResponse;
import io.github.peeyushkumar.bookmyshow.entity.Payment;
import io.github.peeyushkumar.bookmyshow.mapper.PaymentMapper;
import io.github.peeyushkumar.bookmyshow.service.PaymentFacadeService;
import io.github.peeyushkumar.bookmyshow.service.PaymentProcessingService;
import io.github.peeyushkumar.bookmyshow.service.PaymentService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;


/**
 * Orchestrates the complete payment workflow.
 *
 * Why does this class exist?
 *
 * Payment creation and gateway processing intentionally execute in
 * different transactions.
 *
 * Transaction 1
 * -----------------------------
 * - Validate merchant
 * - Create Payment aggregate
 * - Persist Payment
 * - Commit
 *
 * Transaction 2
 * -----------------------------
 * - Call payment gateway
 * - Create/Update PaymentAttempt audit records
 * - Update Payment state
 *
 * Splitting the workflow prevents:
 * - Foreign-key lock timeouts
 * - REQUIRES_NEW visibility issues
 * - Long-running database transactions while waiting on external APIs
 *
 * This follows the "Persist first, Process later" pattern commonly used
 * in production payment systems.
 */
@Service
@AllArgsConstructor
public class PaymentFacadeServiceImpl implements PaymentFacadeService {

    private final PaymentService paymentService;

    private final PaymentProcessingService paymentProcessingService;

    private final PaymentMapper paymentMapper;

    @Override
    public PaymentResponse createPayment(
            CreatePaymentRequest request
    ){
        Long paymentId =
                paymentService.createPayment(request);

        Payment payment = paymentProcessingService
                .processPayment(paymentId);

        return paymentMapper.toResponse(payment);

    }

}
