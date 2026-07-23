package io.github.peeyushkumar.bookmyshow.exception.paymentattempt;

import io.github.peeyushkumar.bookmyshow.enums.FailureReason;
import io.github.peeyushkumar.bookmyshow.exception.base.PaymentAttemptException;
import io.github.peeyushkumar.bookmyshow.exception.code.ErrorCode;
import org.springframework.http.HttpStatus;

public class SignatureVerificationFailedException
        extends PaymentAttemptException {

    public SignatureVerificationFailedException(String providerOrderId) {

        super(
                HttpStatus.BAD_REQUEST,
                ErrorCode.INVALID_PAYMENT_SIGNATURE,
                FailureReason.SIGNATURE_INVALID,
                "Invalid payment signature : " + providerOrderId
        );

    }

}
