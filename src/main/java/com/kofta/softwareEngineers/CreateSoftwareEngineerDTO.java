package com.kofta.softwareEngineers;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.PositiveOrZero;
import java.util.Set;

public record CreateSoftwareEngineerDTO(
    @NotBlank(message = "Name is mandatory") String name,
    @PositiveOrZero(
        message = "Years of experience must be bigger than or equal to zero"
    )
    Integer yearsOfExperience,
    @NotEmpty(message = "Skill IDs are mandatory") Set<Integer> skillIds
) {}
