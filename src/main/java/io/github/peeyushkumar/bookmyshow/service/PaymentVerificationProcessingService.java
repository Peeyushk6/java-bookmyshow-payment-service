package io.github.peeyushkumar.bookmyshow.service;

import io.github.peeyushkumar.bookmyshow.dto.request.PaymentVerificationRequest;
import io.github.peeyushkumar.bookmyshow.dto.response.PaymentVerificationResponse;

public interface PaymentVerificationProcessingService {

    PaymentVerificationResponse process(
            Long paymentId,
            PaymentVerificationRequest request
    );
}
