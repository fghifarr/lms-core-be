package com.ghifar.lms.core.config;

import com.ghifar.lms.core.dto.ErrorResponse;
import jakarta.persistence.OptimisticLockException;
import org.hibernate.exception.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GlobalExceptionHandlerTest {

    @InjectMocks
    private GlobalExceptionHandler globalExceptionHandler;

    @Test
    public void handleException_shouldReturnInternalServerError() {
        Exception ex = new RuntimeException("nullpointer");

        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleException(ex);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Unknown Error", response.getBody().message());
    }

    @Test
    void handleMethodArgumentNotValidException_shouldReturnBadRequestWithErrors() {
        MethodArgumentNotValidException ex = mock(MethodArgumentNotValidException.class);
        BindingResult bindingResult = mock(BindingResult.class);
        FieldError fieldError1 = new FieldError("RegisterBorrowerRequest", "name", "must not be blank");
        FieldError fieldError2 = new FieldError("RegisterBorrowerRequest", "email", "must be a valid email");

        when(ex.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getAllErrors()).thenReturn(List.of(fieldError1, fieldError2));

        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleMethodArgumentNotValidException(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Invalid request", response.getBody().message());
        assertEquals(2, response.getBody().errors().size());
        assertTrue(response.getBody().errors().contains("name: must not be blank"));
        assertTrue(response.getBody().errors().contains("email: must be a valid email"));
    }

    @Test
    void handleDataIntegrityViolationException_withUniqueEmailConstraint_shouldReturnBadRequest() {
        SQLException sqlException = new SQLException("unique constraint violation");
        ConstraintViolationException.ConstraintKind kind = ConstraintViolationException.ConstraintKind.UNIQUE;
        ConstraintViolationException constraintViolationEx =
                new ConstraintViolationException("unique violation", sqlException, kind, "unique_borrower_email");
        DataIntegrityViolationException ex =
                new DataIntegrityViolationException("Data integrity violation", constraintViolationEx);

        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleDataIntegrityViolationException(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Sorry, your email has been used", response.getBody().message());
    }

    @Test
    void handleDataIntegrityViolationException_withNotNullConstraint_shouldReturnBadRequest() {
        SQLException sqlException = new SQLException("not null constraint violation");
        ConstraintViolationException.ConstraintKind kind = ConstraintViolationException.ConstraintKind.NOT_NULL;
        ConstraintViolationException constraintViolationException =
                new ConstraintViolationException("not null violation", sqlException, kind, "name");
        DataIntegrityViolationException exception =
                new DataIntegrityViolationException("Data integrity violation", constraintViolationException);

        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleDataIntegrityViolationException(exception);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("name cannot be null", response.getBody().message());
    }

    @Test
    public void handleObjectOptimisticLockingFailureException_shouldReturnConflict() {
        ObjectOptimisticLockingFailureException ex = new ObjectOptimisticLockingFailureException("Conflict", null);

        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleObjectOptimisticLockingFailureException(ex);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Request conflicted, please try to again", response.getBody().message());
    }

    @Test
    public void handleOptimisticLockException_shouldReturnConflict() {
        OptimisticLockException ex = new OptimisticLockException("Conflict", null);

        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleOptimisticLockException(ex);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Request conflicted, please try to again", response.getBody().message());
    }
}
