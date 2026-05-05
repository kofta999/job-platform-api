package com.kofta.jobapplications;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record CreateJobApplicationDto(@NotNull @Positive Integer postingId) {}
