package io.github.peeyushkumar.bookmyshow.service;

import io.github.peeyushkumar.bookmyshow.dto.request.PaymentVerificationRequest;
import io.github.peeyushkumar.bookmyshow.dto.response.PaymentVerificationResponse;

public interface PaymentVerificationFacadeService {

    PaymentVerificationResponse verify(
            PaymentVerificationRequest request
    );

}
