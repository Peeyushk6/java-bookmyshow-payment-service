package io.github.peeyushkumar.bookmyshow.exception.response;

import io.github.peeyushkumar.bookmyshow.exception.code.ErrorCode;
import lombok.Builder;

import java.time.Instant;

@Builder
public class ErrorResponse
{
    private Instant timestamp;

    private int status;

    private String error;

    private ErrorCode errorCode;

    private String message;

    private String correlationId;

    private String path;

}
