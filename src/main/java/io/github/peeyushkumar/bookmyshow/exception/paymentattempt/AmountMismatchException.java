package io.github.peeyushkumar.bookmyshow.exception.paymentattempt;

import io.github.peeyushkumar.bookmyshow.enums.FailureReason;
import io.github.peeyushkumar.bookmyshow.exception.base.PaymentAttemptException;
import io.github.peeyushkumar.bookmyshow.exception.code.ErrorCode;
import org.springframework.http.HttpStatus;

public class AmountMismatchException
        extends PaymentAttemptException {

    public AmountMismatchException(String providerOrderId) {

        super(
                HttpStatus.BAD_REQUEST,
                ErrorCode.AMOUNT_MISMATCH,
                FailureReason.AMOUNT_MISMATCH,
                "Payment amount mismatch : " + providerOrderId
        );

    }

}