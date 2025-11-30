package com.ghifar.lms.core.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public class ValidationException extends RuntimeException {

    private final HttpStatus status;

    public ValidationException(HttpStatus responseStatus, String message) {
        super(message);
        status = responseStatus;
    }
}
