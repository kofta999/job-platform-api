package com.kofta.errors;

import jakarta.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiError> handleSoftwareEngineerNotFound(
        ResourceNotFoundException ex,
        HttpServletRequest request
    ) {
        var error = ApiError.of(
            HttpStatus.NOT_FOUND,
            ex.getMessage(),
            request.getRequestURI(),
            null
        );

        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidationExceptions(
        MethodArgumentNotValidException ex,
        HttpServletRequest request
    ) {
        Map<String, String> fieldErrors = new HashMap<>();
        ex
            .getBindingResult()
            .getAllErrors()
            .forEach(error -> {
                String fieldName = ((FieldError) error).getField();
                String errorMessage = error.getDefaultMessage();
                fieldErrors.put(fieldName, errorMessage);
            });

        var error = ApiError.of(
            HttpStatus.BAD_REQUEST,
            "Validation failed",
            request.getRequestURI(),
            fieldErrors
        );

        return new ResponseEntity<ApiError>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiError> handleBadCredentialsException(
        BadCredentialsException ex,
        HttpServletRequest request
    ) {
        var error = ApiError.of(
            HttpStatus.UNAUTHORIZED,
            ex.getMessage(),
            request.getRequestURI(),
            null
        );

        return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(DisabledException.class)
    public ResponseEntity<ApiError> handleUserDisabledException(
        DisabledException ex,
        HttpServletRequest request
    ) {
        var error = ApiError.of(
            HttpStatus.UNAUTHORIZED,
            ex.getMessage(),
            request.getRequestURI(),
            null
        );

        return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(AuthorizationDeniedException.class)
    public ResponseEntity<ApiError> handleAuthDeniedException(
        AuthorizationDeniedException ex,
        HttpServletRequest request
    ) {
        var error = ApiError.of(
            HttpStatus.FORBIDDEN,
            ex.getMessage(),
            request.getRequestURI(),
            null
        );

        return new ResponseEntity<>(error, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(ResourceAlreadyExists.class)
    public ResponseEntity<ApiError> handleResourceAlreadyExists(
        ResourceAlreadyExists ex,
        HttpServletRequest request
    ) {
        var error = ApiError.of(
            HttpStatus.CONFLICT,
            ex.getMessage(),
            request.getRequestURI(),
            null
        );

        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(InvalidStatusTransitionException.class)
    public ResponseEntity<ApiError> handleInvalidStatusTransitionException(
        InvalidStatusTransitionException ex,
        HttpServletRequest request
    ) {
        var error = ApiError.of(
            HttpStatus.UNPROCESSABLE_ENTITY,
            ex.getMessage(),
            request.getRequestURI(),
            null
        );

        return new ResponseEntity<>(error, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler
    public ResponseEntity<ApiError> catchAll(
        RuntimeException ex,
        HttpServletRequest request
    ) {
        var error = ApiError.of(
            HttpStatus.INTERNAL_SERVER_ERROR,
            // TODO: Replace with "Something went wrong" after testing
            ex.getMessage(),
            request.getRequestURI(),
            null
        );

        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
