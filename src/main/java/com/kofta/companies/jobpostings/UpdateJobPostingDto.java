package com.kofta.companies.jobpostings;

import com.kofta.skills.Skill;
import java.util.Set;

public record UpdateJobPostingDto(
    String title,
    String description,
    Integer salary,
    Set<Skill> skills
) {}
