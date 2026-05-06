package com.kofta.auth;

import com.kofta.softwareengineers.CreateSoftwareEngineerDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record RegisterEngineerDto(
    @NotBlank @Email String email,
    @NotBlank String password,
    @Valid CreateSoftwareEngineerDto profile
) {}
