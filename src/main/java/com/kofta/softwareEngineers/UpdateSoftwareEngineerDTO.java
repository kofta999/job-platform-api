package com.kofta.softwareEngineers;

import jakarta.validation.constraints.PositiveOrZero;
import java.util.Set;

public record UpdateSoftwareEngineerDTO(
    String name,
    @PositiveOrZero(
        message = "Years of experience must be bigger than or equal to zero"
    )
    Integer yearsOfExperience,
    Set<Integer> skillIds
) {}
