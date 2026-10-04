package io.github.peeyushkumar.bookmyshow.service;

import io.github.peeyushkumar.bookmyshow.dto.request.CreatePaymentRequest;
import io.github.peeyushkumar.bookmyshow.dto.request.PaymentVerificationRequest;

public interface PaymentService {

    Long createPayment(CreatePaymentRequest request);

}
