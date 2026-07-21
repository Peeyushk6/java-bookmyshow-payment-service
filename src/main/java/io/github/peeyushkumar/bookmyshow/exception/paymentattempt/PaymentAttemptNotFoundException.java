package io.github.peeyushkumar.bookmyshow.exception.paymentattempt;

import io.github.peeyushkumar.bookmyshow.exception.base.PaymentServiceException;
import io.github.peeyushkumar.bookmyshow.exception.code.ErrorCode;
import org.springframework.http.HttpStatus;

public class PaymentAttemptNotFoundException extends PaymentServiceException {
    public PaymentAttemptNotFoundException(
            Long attemptId
    ) {

        super(
                HttpStatus.NOT_FOUND,
                ErrorCode.PAYMENT_ATTEMPT_NOT_FOUND,
                "Payment attempt not found: " + attemptId
        );

    }
}
