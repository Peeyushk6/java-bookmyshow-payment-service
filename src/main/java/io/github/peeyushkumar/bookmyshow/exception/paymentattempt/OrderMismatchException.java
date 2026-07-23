package io.github.peeyushkumar.bookmyshow.exception.paymentattempt;

import io.github.peeyushkumar.bookmyshow.enums.FailureReason;
import io.github.peeyushkumar.bookmyshow.exception.base.PaymentAttemptException;
import io.github.peeyushkumar.bookmyshow.exception.code.ErrorCode;
import org.springframework.http.HttpStatus;

public class OrderMismatchException
        extends PaymentAttemptException {

    public OrderMismatchException(String providerOrderId) {

        super(
                HttpStatus.BAD_REQUEST,
                ErrorCode.ORDER_ID_MISMATCH,
                FailureReason.ORDER_ID_MISMATCH,
                "Provider order mismatch : " + providerOrderId
        );

    }

}
