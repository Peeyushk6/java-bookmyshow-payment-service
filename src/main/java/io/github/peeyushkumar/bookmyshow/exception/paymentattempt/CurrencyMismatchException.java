package io.github.peeyushkumar.bookmyshow.exception.paymentattempt;

import io.github.peeyushkumar.bookmyshow.enums.FailureReason;
import io.github.peeyushkumar.bookmyshow.exception.base.PaymentAttemptException;
import io.github.peeyushkumar.bookmyshow.exception.code.ErrorCode;
import org.springframework.http.HttpStatus;

public class CurrencyMismatchException
        extends PaymentAttemptException {

    public CurrencyMismatchException(String providerOrderId) {

        super(
                HttpStatus.BAD_REQUEST,
                ErrorCode.CURRENCY_MISMATCH,
                FailureReason.CURRENCY_MISMATCH,
                "Payment currency mismatch : " + providerOrderId
        );

    }

}
