package com.kofta.companies.jobpostings;

import com.kofta.skills.Skill;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.util.Set;
import org.hibernate.validator.constraints.Length;

public record CreateJobPostingDto(
    @NotBlank @Length(min = 10) String title,
    @NotBlank @Length(min = 30) String description,
    @NotNull @Positive Integer salary,
    @NotEmpty Set<Skill> skills
) {}
