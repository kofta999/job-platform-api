package com.kofta.auth;

import jakarta.validation.constraints.NotEmpty;

public record RegisterDto(
    @NotEmpty String username,
    @NotEmpty String password
) {}
