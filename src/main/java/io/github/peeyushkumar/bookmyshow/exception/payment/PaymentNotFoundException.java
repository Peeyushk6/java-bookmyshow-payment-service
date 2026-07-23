package io.github.peeyushkumar.bookmyshow.exception.payment;

import io.github.peeyushkumar.bookmyshow.enums.FailureReason;
import io.github.peeyushkumar.bookmyshow.exception.base.PaymentAttemptException;
import io.github.peeyushkumar.bookmyshow.exception.code.ErrorCode;
import org.springframework.http.HttpStatus;

public class PaymentNotFoundException extends PaymentAttemptException {
    public PaymentNotFoundException(String providerOrderId){
        super(
                HttpStatus.NOT_FOUND,
                ErrorCode.PAYMENT_NOT_FOUND,
                FailureReason.PAYMENT_NOT_FOUND,
                "Payment not found : " + providerOrderId
        );
    }
}
