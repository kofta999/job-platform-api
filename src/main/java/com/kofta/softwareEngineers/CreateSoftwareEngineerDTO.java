package com.kofta.softwareEngineers;

import jakarta.validation.constraints.NotBlank;

public record CreateSoftwareEngineerDTO(
    @NotBlank(message = "Name is mandatory") String name,
    @NotBlank(message = "Tech Stack is mandatory") String techStack
) {}
