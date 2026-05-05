package com.kofta.softwareengineers;

import com.kofta.engineerprofiles.EngineerProfileDto;
import com.kofta.skills.SkillDto;
import java.util.Set;

public record SoftwareEngineerWithProfileDto(
    Integer id,
    String name,
    Set<SkillDto> skills,
    EngineerProfileDto profile,
    Integer yearsOfExperience
) {}
