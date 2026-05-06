package com.kofta.auth;

import jakarta.validation.constraints.NotEmpty;

public record LoginDto(@NotEmpty String username, @NotEmpty String password) {}
