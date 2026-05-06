package com.kofta.companies.jobpostings;

import com.kofta.skills.SkillDto;
import java.util.Set;

public record JobPostingDetailsDto(
    Integer id,
    String title,
    String description,
    Integer salary,
    Set<SkillDto> skills
) {}
