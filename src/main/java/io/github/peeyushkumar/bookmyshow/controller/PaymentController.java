package io.github.peeyushkumar.bookmyshow.controller;

import io.github.peeyushkumar.bookmyshow.dto.request.CreatePaymentRequest;
import io.github.peeyushkumar.bookmyshow.dto.request.PaymentVerificationRequest;
import io.github.peeyushkumar.bookmyshow.dto.response.PaymentResponse;
import io.github.peeyushkumar.bookmyshow.dto.response.PaymentVerificationResponse;
import io.github.peeyushkumar.bookmyshow.service.PaymentFacadeService;
import io.github.peeyushkumar.bookmyshow.service.PaymentVerificationFacadeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentFacadeService paymentFacadeService;
    private final PaymentVerificationFacadeService paymentVerificationFacadeService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PaymentResponse createPayment(
            @Valid
            @RequestBody
            CreatePaymentRequest request
    ){
        return paymentFacadeService.createPayment(request);
    }

    @PostMapping("/verify")
    public ResponseEntity<PaymentVerificationResponse> verify(
            @Valid
            @RequestBody
            PaymentVerificationRequest request
    ) {

        return ResponseEntity.ok(
                paymentVerificationFacadeService.verify(request)
        );
    }

}
