package com.accenture.supermarket.exception;

import org.springframework.http.HttpStatus;

import java.time.Instant;
import java.util.Collections;
import java.util.List;

public record ApiError(
        int status,
        String error,
        String message,
        String path,
        Instant timestamp,
        List<FieldErrorDetail> details
) {

    public ApiError(int status, String error, String message, String path, Instant timestamp) {
        this(status, error, message, path, timestamp, Collections.emptyList());
    }

    public static ApiError of(HttpStatus status, String message, String path) {
        return new ApiError(
                status.value(),
                status.getReasonPhrase(),
                message,
                path,
                Instant.now()
        );
    }

    public static ApiError validation(List<FieldErrorDetail> details, String path) {
        return new ApiError(
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                "Falha na validação dos dados",
                path,
                Instant.now(),
                details
        );
    }
}
