package com.kofta.auth;

import com.kofta.companies.CreateCompanyDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record RegisterCompanyDto(
    @NotBlank @Email String email,
    @NotBlank String password,
    @Valid CreateCompanyDto companyDetails
) {}
