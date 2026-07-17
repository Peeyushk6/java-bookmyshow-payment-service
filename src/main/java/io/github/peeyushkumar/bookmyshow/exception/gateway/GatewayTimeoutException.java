package io.github.peeyushkumar.bookmyshow.exception.gateway;

import io.github.peeyushkumar.bookmyshow.exception.base.PaymentServiceException;
import io.github.peeyushkumar.bookmyshow.exception.code.ErrorCode;
import org.springframework.http.HttpStatus;

public class GatewayTimeoutException
        extends PaymentServiceException {

    public GatewayTimeoutException() {

        super(
                HttpStatus.GATEWAY_TIMEOUT,
                ErrorCode.GATEWAY_TIMEOUT,
                "Gateway timeout"
        );

    }

}
