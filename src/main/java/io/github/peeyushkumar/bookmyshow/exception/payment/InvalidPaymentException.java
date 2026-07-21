package io.github.peeyushkumar.bookmyshow.exception.payment;

import io.github.peeyushkumar.bookmyshow.exception.base.PaymentServiceException;
import io.github.peeyushkumar.bookmyshow.exception.code.ErrorCode;
import org.springframework.http.HttpStatus;

public class InvalidPaymentException extends PaymentServiceException {

    public InvalidPaymentException(String providerOrderId){
            super(HttpStatus.NOT_FOUND,
                    ErrorCode.PAYMENT_NOT_FOUND,
                    "providerOrderId not found: " + providerOrderId);
        }
}
