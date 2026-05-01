package com.kofta;

import com.kofta.softwareEngineers.SoftwareEngineerNotFoundException;
import java.time.LocalDateTime;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(SoftwareEngineerNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleSoftwareEngineerNotFound(
        SoftwareEngineerNotFoundException ex
    ) {
        var error = new ErrorResponse(
            HttpStatus.NOT_FOUND,
            ex.getMessage(),
            LocalDateTime.now()
        );

        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> catchAll(RuntimeException ex) {
        var error = new ErrorResponse(
            HttpStatus.INTERNAL_SERVER_ERROR,
            ex.getMessage(),
            LocalDateTime.now()
        );

        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
