package io.github.peeyushkumar.bookmyshow.controller;

import io.github.peeyushkumar.bookmyshow.dto.request.CreatePaymentRequest;
import io.github.peeyushkumar.bookmyshow.dto.request.PaymentVerificationRequest;
import io.github.peeyushkumar.bookmyshow.dto.response.PaymentResponse;
import io.github.peeyushkumar.bookmyshow.dto.response.PaymentVerificationResponse;
import io.github.peeyushkumar.bookmyshow.service.PaymentService;
import io.github.peeyushkumar.bookmyshow.service.impl.PaymentVerificationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;
    private final PaymentVerificationService paymentVerificationService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PaymentResponse createPayment(
            @Valid
            @RequestBody
            CreatePaymentRequest request
    ){
        return paymentService.createPayment(request);
    }

    @PostMapping("/verify")
    public ResponseEntity<PaymentVerificationResponse> verifyPayment(
            @Valid @RequestBody PaymentVerificationRequest request) {

        return ResponseEntity.ok(
                paymentVerificationService.verify(request)
        );
    }

}
