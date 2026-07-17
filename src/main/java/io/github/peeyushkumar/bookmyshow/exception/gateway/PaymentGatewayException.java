package io.github.peeyushkumar.bookmyshow.exception.gateway;

import io.github.peeyushkumar.bookmyshow.exception.base.PaymentServiceException;
import io.github.peeyushkumar.bookmyshow.exception.code.ErrorCode;
import org.springframework.http.HttpStatus;

public class PaymentGatewayException
        extends PaymentServiceException {

    public PaymentGatewayException(String message) {

        super(
                HttpStatus.BAD_GATEWAY,
                ErrorCode.PAYMENT_GATEWAY_ERROR,
                message
        );

    }

}
