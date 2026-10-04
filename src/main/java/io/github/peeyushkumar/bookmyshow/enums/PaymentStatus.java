package io.github.peeyushkumar.bookmyshow.enums;

/*
INITIATED → user wants to pay
PROCESSING → gateway order created / verification in progress
SUCCESS → payment captured
FAILED → permanently failed
**/
public enum PaymentStatus {
    PENDING,
    INITIATED,
    PROCESSING,
    SUCCESS,
    FAILED,
    CANCELLED,
    REFUNDED,
    PARTIALLY_REFUNDED,
    EXPIRED
}
