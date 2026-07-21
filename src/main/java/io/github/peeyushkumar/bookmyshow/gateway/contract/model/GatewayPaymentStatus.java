package io.github.peeyushkumar.bookmyshow.gateway.contract.model;

public enum GatewayPaymentStatus {
    CREATED,
    AUTHORIZED,
    CAPTURED,
    FAILED,
    REFUNDED,
    CANCELLED,
    PENDING,
    UNKNOWN
}
