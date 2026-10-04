package io.github.peeyushkumar.bookmyshow.exception.payment;

import io.github.peeyushkumar.bookmyshow.exception.base.PaymentServiceException;
import io.github.peeyushkumar.bookmyshow.exception.code.ErrorCode;
import org.springframework.http.HttpStatus;

public class InvalidPaymentStateException extends PaymentServiceException {

    public InvalidPaymentStateException(Long paymentId){

        super(HttpStatus.NOT_FOUND,
                ErrorCode.INVALID_PAYMENT_STATE,
                "Payment not Initiated : " + paymentId
        );
    }
}
