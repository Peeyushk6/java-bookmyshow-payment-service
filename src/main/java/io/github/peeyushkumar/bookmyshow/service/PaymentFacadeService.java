package io.github.peeyushkumar.bookmyshow.service;

import io.github.peeyushkumar.bookmyshow.dto.request.CreatePaymentRequest;
import io.github.peeyushkumar.bookmyshow.dto.response.PaymentResponse;

public interface PaymentFacadeService {

    PaymentResponse createPayment(CreatePaymentRequest request);

    interface VerificationProcessingService {
    }
}
