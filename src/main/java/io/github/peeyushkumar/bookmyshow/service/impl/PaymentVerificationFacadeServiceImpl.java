package io.github.peeyushkumar.bookmyshow.service.impl;

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
public class PaymentVerificationFacadeServiceImpl
{
}
