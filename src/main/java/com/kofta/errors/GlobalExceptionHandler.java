package com.kofta.errors;

import jakarta.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
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
    public ResponseEntity<ProblemDetail> handleSoftwareEngineerNotFound(
        ResourceNotFoundException ex,
        HttpServletRequest request
    ) {
        var problem = buildProblemDetail(
            HttpStatus.NOT_FOUND,
            ex.getMessage(),
            request
        );

        return new ResponseEntity<>(problem, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ProblemDetail> handleValidationExceptions(
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

        var problem = buildProblemDetail(
            HttpStatus.BAD_REQUEST,
            "Validation failed",
            request
        );
        problem.setProperty("fieldErrors", fieldErrors);

        return new ResponseEntity<>(problem, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ProblemDetail> handleBadCredentialsException(
        BadCredentialsException ex,
        HttpServletRequest request
    ) {
        var problem = buildProblemDetail(
            HttpStatus.UNAUTHORIZED,
            ex.getMessage(),
            request
        );

        return new ResponseEntity<>(problem, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(DisabledException.class)
    public ResponseEntity<ProblemDetail> handleUserDisabledException(
        DisabledException ex,
        HttpServletRequest request
    ) {
        var problem = buildProblemDetail(
            HttpStatus.UNAUTHORIZED,
            ex.getMessage(),
            request
        );

        return new ResponseEntity<>(problem, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(AuthorizationDeniedException.class)
    public ResponseEntity<ProblemDetail> handleAuthDeniedException(
        AuthorizationDeniedException ex,
        HttpServletRequest request
    ) {
        var problem = buildProblemDetail(
            HttpStatus.FORBIDDEN,
            ex.getMessage(),
            request
        );

        return new ResponseEntity<>(problem, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(ResourceAlreadyExists.class)
    public ResponseEntity<ProblemDetail> handleResourceAlreadyExists(
        ResourceAlreadyExists ex,
        HttpServletRequest request
    ) {
        var problem = buildProblemDetail(
            HttpStatus.CONFLICT,
            ex.getMessage(),
            request
        );

        return new ResponseEntity<>(problem, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(InvalidStatusTransitionException.class)
    public ResponseEntity<ProblemDetail> handleInvalidStatusTransitionException(
        InvalidStatusTransitionException ex,
        HttpServletRequest request
    ) {
        var problem = buildProblemDetail(
            HttpStatus.UNPROCESSABLE_ENTITY,
            ex.getMessage(),
            request
        );

        return new ResponseEntity<>(problem, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler
    public ResponseEntity<ProblemDetail> catchAll(
        RuntimeException ex,
        HttpServletRequest request
    ) {
        var problem = buildProblemDetail(
            HttpStatus.INTERNAL_SERVER_ERROR,
            // TODO: Replace with "Something went wrong" after testing
            ex.getMessage(),
            request
        );

        return new ResponseEntity<>(problem, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ProblemDetail buildProblemDetail(
        HttpStatus status,
        String detail,
        HttpServletRequest request
    ) {
        var problem = ProblemDetail.forStatusAndDetail(status, detail);
        problem.setTitle(status.getReasonPhrase());
        problem.setProperty("path", request.getRequestURI());
        return problem;
    }
}
