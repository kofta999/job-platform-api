package com.kofta;

import com.kofta.errors.ApiError;
import com.kofta.softwareEngineers.SoftwareEngineerNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(SoftwareEngineerNotFoundException.class)
    public ResponseEntity<ApiError> handleSoftwareEngineerNotFound(
        SoftwareEngineerNotFoundException ex,
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

    @ExceptionHandler
    public ResponseEntity<ApiError> catchAll(
        RuntimeException ex,
        HttpServletRequest request
    ) {
        var error = ApiError.of(
            HttpStatus.INTERNAL_SERVER_ERROR,
            "Something went wrong",
            request.getRequestURI(),
            null
        );

        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
