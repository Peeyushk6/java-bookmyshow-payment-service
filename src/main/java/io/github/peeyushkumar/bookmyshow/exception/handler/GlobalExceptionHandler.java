package io.github.peeyushkumar.bookmyshow.exception.handler;

import io.github.peeyushkumar.bookmyshow.exception.base.PaymentServiceException;
import io.github.peeyushkumar.bookmyshow.exception.code.ErrorCode;
import io.github.peeyushkumar.bookmyshow.util.CorrelationIdHolder;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import io.github.peeyushkumar.bookmyshow.exception.response.ErrorResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(PaymentServiceException.class)
    public ResponseEntity<ErrorResponse> handle(
            PaymentServiceException ex,
            HttpServletRequest request) {

        ErrorResponse response =
                ErrorResponse.builder()
                        .timestamp(Instant.now())
                        .status(ex.getStatus().value())
                        .error(ex.getStatus().getReasonPhrase())
                        .errorCode(ex.getErrorCode())
                        .message(ex.getMessage())
                        .path(request.getRequestURI())
                        .correlationId(
                                MDC.get(
                                        CorrelationIdHolder.CORRELATION_ID
                                )
                        )
                        .build();

        return ResponseEntity
                .status(ex.getStatus())
                .body(response);

    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handle(
            Exception ex,
            HttpServletRequest request) {

        ErrorResponse response =
                ErrorResponse.builder()
                        .timestamp(Instant.now())
                        .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                        .error(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())
                        .errorCode(ErrorCode.INTERNAL_SERVER_ERROR)
                        .message(ex.getMessage())
                        .path(request.getRequestURI())
                        .correlationId(
                                MDC.get(
                                        CorrelationIdHolder.CORRELATION_ID
                                )
                        )
                        .build();

        return ResponseEntity
                .internalServerError()
                .body(response);

    }

}
