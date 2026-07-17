package io.github.peeyushkumar.bookmyshow.exception.payment;

import io.github.peeyushkumar.bookmyshow.exception.base.PaymentServiceException;
import io.github.peeyushkumar.bookmyshow.exception.code.ErrorCode;
import org.springframework.http.HttpStatus;

public class DuplicatePaymentException
        extends PaymentServiceException {

    public DuplicatePaymentException(String idempotencyKey) {

        super(
                HttpStatus.CONFLICT,
                ErrorCode.DUPLICATE_PAYMENT,
                "Duplicate payment request : " + idempotencyKey
        );

    }

}
