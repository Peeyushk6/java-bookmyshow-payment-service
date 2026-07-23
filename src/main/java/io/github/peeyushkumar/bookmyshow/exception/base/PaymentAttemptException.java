package io.github.peeyushkumar.bookmyshow.exception.base;

import io.github.peeyushkumar.bookmyshow.enums.FailureReason;
import io.github.peeyushkumar.bookmyshow.exception.code.ErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;


@Getter
public class PaymentAttemptException extends PaymentServiceException {

    private final FailureReason failureReason;

    protected PaymentAttemptException(
            HttpStatus status,
            ErrorCode errorCode,
            FailureReason failureReason,
            String message
    ) {
        super(status, errorCode, message);
        this.failureReason = failureReason;
    }

    protected PaymentAttemptException(
            HttpStatus status,
            ErrorCode errorCode,
            FailureReason failureReason,
            String message,
            Throwable cause
    ) {
        super(status, errorCode, message, cause);
        this.failureReason = failureReason;
    }
}