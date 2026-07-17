package io.github.peeyushkumar.bookmyshow.exception.base;

import io.github.peeyushkumar.bookmyshow.exception.code.ErrorCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
public abstract class PaymentServiceException extends RuntimeException
{
    private final HttpStatus status;

    private final ErrorCode errorCode;

    protected PaymentServiceException(
            HttpStatus status,
            ErrorCode errorCode,
            String message
    ){
        super(message);
        this.status = status;
        this.errorCode = errorCode;
    }
}
