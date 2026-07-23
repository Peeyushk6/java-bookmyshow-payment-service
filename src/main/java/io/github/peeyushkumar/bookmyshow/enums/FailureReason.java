package io.github.peeyushkumar.bookmyshow.enums;

public enum FailureReason {

    // Validation failures
    SIGNATURE_INVALID,
    AMOUNT_MISMATCH,
    CURRENCY_MISMATCH,
    ORDER_ID_MISMATCH,

    // Resource failures
    PAYMENT_NOT_FOUND,
    PROVIDER_PAYMENT_NOT_FOUND,

    // Gateway failures
    GATEWAY_TIMEOUT,
    GATEWAY_ERROR,

    // User / Provider failures
    PAYMENT_DECLINED,
    USER_CANCELLED,

    // Internal failures
    INTERNAL_ERROR,

    // Fallback
    UNKNOWN

}