package com.kofta.softwareEngineers;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;

public record CreateSoftwareEngineerDTO(
    @NotBlank(message = "Name is mandatory") String name,
    @NotBlank(message = "Tech Stack is mandatory") String techStack,
    @PositiveOrZero(
        message = "Years of experience must be bigger than or equal to zero"
    )
    Integer yearsOfExperience
) {}
