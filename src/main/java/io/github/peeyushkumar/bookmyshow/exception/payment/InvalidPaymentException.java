package io.github.peeyushkumar.bookmyshow.exception.payment;

import io.github.peeyushkumar.bookmyshow.exception.base.PaymentServiceException;
import io.github.peeyushkumar.bookmyshow.exception.code.ErrorCode;
import org.springframework.http.HttpStatus;

public class InvalidPaymentException
        extends PaymentServiceException {

    public InvalidPaymentException(String message) {

        super(
                HttpStatus.BAD_REQUEST,
                ErrorCode.INVALID_PAYMENT,
                message
        );

    }

}