package io.github.peeyushkumar.bookmyshow.enums;

public enum FailureReason {

    SIGNATURE_INVALID,

    AMOUNT_MISMATCH,

    CURRENCY_MISMATCH,

    ORDER_ID_MISMATCH,

    PAYMENT_NOT_FOUND,

    PROVIDER_PAYMENT_NOT_FOUND,

    GATEWAY_TIMEOUT,

    GATEWAY_ERROR,

    UNKNOWN

}