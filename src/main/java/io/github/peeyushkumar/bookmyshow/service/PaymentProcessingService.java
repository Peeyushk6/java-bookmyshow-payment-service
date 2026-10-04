package io.github.peeyushkumar.bookmyshow.service;

import io.github.peeyushkumar.bookmyshow.entity.Payment;

public interface PaymentProcessingService {
    public Payment processPayment(Long paymentId);
}
