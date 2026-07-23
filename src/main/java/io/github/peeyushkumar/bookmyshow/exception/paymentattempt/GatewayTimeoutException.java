package io.github.peeyushkumar.bookmyshow.exception.paymentattempt;

import io.github.peeyushkumar.bookmyshow.enums.FailureReason;
import io.github.peeyushkumar.bookmyshow.exception.base.PaymentAttemptException;
import io.github.peeyushkumar.bookmyshow.exception.code.ErrorCode;
import org.springframework.http.HttpStatus;

public class GatewayTimeoutException
        extends PaymentAttemptException {

    public GatewayTimeoutException(Throwable cause) {

        super(
                HttpStatus.BAD_GATEWAY,
                ErrorCode.GATEWAY_TIMEOUT,
                FailureReason.GATEWAY_TIMEOUT,
                "Gateway timeout",
                cause
        );

    }

}
