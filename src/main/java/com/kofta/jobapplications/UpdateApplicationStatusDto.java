package com.kofta.jobapplications;

import jakarta.validation.constraints.NotNull;

public record UpdateApplicationStatusDto(
    @NotNull JobApplicationStatus newStatus
) {}
