package com.kofta.companies;

import jakarta.validation.constraints.NotBlank;

public record CreateCompanyDto(
    @NotBlank String name,
    @NotBlank String hqLocation
) {}
