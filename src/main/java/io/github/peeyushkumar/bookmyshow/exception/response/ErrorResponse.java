package io.github.peeyushkumar.bookmyshow.exception.response;

import io.github.peeyushkumar.bookmyshow.exception.code.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.Instant;


@Builder
public record ErrorResponse(
        Instant timestamp,
        Integer status,
        String error,
        ErrorCode errorCode,
        String message,
        String correlationId,
        String path
){}