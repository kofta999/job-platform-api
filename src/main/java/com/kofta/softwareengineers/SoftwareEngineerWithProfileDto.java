package com.kofta.softwareengineers;

import com.kofta.engineerprofiles.EngineerProfile;
import com.kofta.skills.SkillDto;
import java.util.Set;

public record SoftwareEngineerWithProfileDto(
    Integer id,
    String name,
    Set<SkillDto> skills,
    EngineerProfile profile,
    Integer yearsOfExperience
) {}
