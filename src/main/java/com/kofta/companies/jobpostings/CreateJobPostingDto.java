package com.kofta.companies.jobpostings;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.util.List;
import org.hibernate.validator.constraints.Length;

public record CreateJobPostingDto(
    @NotBlank @Length(min = 10) String title,
    @NotBlank @Length(min = 30) String description,
    @NotNull @Positive Integer salary,
    @NotEmpty List<Integer> skillIds
) {}
