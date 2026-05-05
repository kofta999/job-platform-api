package com.kofta.softwareengineers;

import com.kofta.skills.SkillDto;
import java.util.Set;

public record SoftwareEngineerDto(
    Integer id,
    String name,
    Set<SkillDto> skills,
    Integer yearsOfExperience
) {}
