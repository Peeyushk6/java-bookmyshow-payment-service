package io.github.peeyushkumar.bookmyshow.service;

import io.github.peeyushkumar.bookmyshow.dto.request.PaymentVerificationRequest;

public interface PaymentVerificationService {

    Long verify(
            PaymentVerificationRequest request
    );
}
