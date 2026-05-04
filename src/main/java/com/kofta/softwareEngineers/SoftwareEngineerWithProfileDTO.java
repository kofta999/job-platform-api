package com.kofta.softwareEngineers;

import com.kofta.engineerProfiles.EngineerProfile;
import com.kofta.skills.SkillDTO;
import java.util.Set;

public record SoftwareEngineerWithProfileDTO(
    Integer id,
    String name,
    Set<SkillDTO> skills,
    EngineerProfile profile,
    Integer yearsOfExperience
) {}
