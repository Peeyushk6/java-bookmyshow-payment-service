package io.github.peeyushkumar.bookmyshow.exception.paymentattempt;

import io.github.peeyushkumar.bookmyshow.enums.FailureReason;
import io.github.peeyushkumar.bookmyshow.exception.base.PaymentAttemptException;
import io.github.peeyushkumar.bookmyshow.exception.code.ErrorCode;
import org.springframework.http.HttpStatus;

public class PaymentNotFoundException extends PaymentAttemptException {
    public PaymentNotFoundException(Long paymentId){
        super(
                HttpStatus.NOT_FOUND,
                ErrorCode.PAYMENT_NOT_FOUND,
                FailureReason.PAYMENT_NOT_FOUND,
                "Payment not found : " + paymentId
        );
    }
}
