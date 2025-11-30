package com.ghifar.lms.core.config;

import com.ghifar.lms.core.common.exception.ValidationException;
import com.ghifar.lms.core.dto.ErrorResponse;
import jakarta.persistence.OptimisticLockException;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception ex) {
        log.error("handleException() with exception: {}", ex.getMessage(), ex);
        ErrorResponse resp = new ErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Unknown Error",
                null
        );

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(resp);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        log.debug("handleMethodArgumentNotValidException() with exception: {}", ex.getMessage());
        List<String> errors = new ArrayList<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.add(String.format("%s: %s", fieldName, errorMessage));
        });
        ErrorResponse resp = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Invalid request",
                errors
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resp);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
        log.debug("handleDataIntegrityViolationException() with exception: {}", ex.getMessage());

        String respMessage;
        HttpStatus respStatus;
        Throwable cause = ex.getCause();

        if (cause instanceof ConstraintViolationException constraintViolationEx) {
            ConstraintViolationException.ConstraintKind kind = constraintViolationEx.getKind();
            String constraintName = constraintViolationEx.getConstraintName();
            switch (kind) {
                case UNIQUE -> {
                    if ("unique_borrower_email".equals(constraintName)) {
                        respStatus = HttpStatus.BAD_REQUEST;
                        respMessage = "Sorry, your email has been used";
                    } else {
                        log.warn("unhandled unique constraints: {}", constraintName);
                        respStatus = HttpStatus.INTERNAL_SERVER_ERROR;
                        respMessage = "Unknown error";
                    }
                }
                case NOT_NULL -> {
                    respStatus = HttpStatus.BAD_REQUEST;
                    respMessage = String.format("%s cannot be null", constraintName);
                }
                default -> {
                    log.warn("unhandled kind: {}", kind);
                    respStatus = HttpStatus.INTERNAL_SERVER_ERROR;
                    respMessage = "Unknown error";
                }
            }
        } else {
            log.warn("unhandled");
            respStatus = HttpStatus.INTERNAL_SERVER_ERROR;
            respMessage = "Unknown error";
        }

        ErrorResponse resp = new ErrorResponse(
                respStatus.value(),
                respMessage,
                null
        );

        return ResponseEntity.status(respStatus).body(resp);
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(ValidationException ex) {
        log.debug("handleValidationException() with exception: {}", ex.getMessage());
        ErrorResponse resp = new ErrorResponse(
                ex.getStatus().value(),
                ex.getMessage(),
                null
        );

        return ResponseEntity.status(ex.getStatus()).body(resp);
    }

    @ExceptionHandler(ObjectOptimisticLockingFailureException.class)
    public ResponseEntity<ErrorResponse> handleObjectOptimisticLockingFailureException(ObjectOptimisticLockingFailureException ex) {
        log.warn("handleObjectOptimisticLockingFailureException() with exception: {}", ex.getMessage(), ex);
        ErrorResponse resp = new ErrorResponse(
                HttpStatus.CONFLICT.value(),
                "Request conflicted, please try to again",
                null
        );

        return ResponseEntity.status(HttpStatus.CONFLICT).body(resp);
    }

    @ExceptionHandler(OptimisticLockException.class)
    public ResponseEntity<ErrorResponse> handleOptimisticLockException(OptimisticLockException ex) {
        log.warn("handleOptimisticLockException() with exception: {}", ex.getMessage(), ex);
        ErrorResponse resp = new ErrorResponse(
                HttpStatus.CONFLICT.value(),
                "Request conflicted, please try to again",
                null
        );

        return ResponseEntity.status(HttpStatus.CONFLICT).body(resp);
    }
}
