package com.kofta;

import java.time.LocalDateTime;
import org.springframework.http.HttpStatus;

public record ErrorResponse(
    HttpStatus status,
    String message,
    LocalDateTime timestamp
) {}
