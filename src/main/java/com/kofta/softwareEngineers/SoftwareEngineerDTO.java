package com.kofta.softwareEngineers;

import com.kofta.engineerProfiles.EngineerProfileDTO;
import com.kofta.skills.SkillDTO;
import java.util.Set;

public record SoftwareEngineerDTO(
    Integer id,
    String name,
    Set<SkillDTO> skills,
    EngineerProfileDTO profile,
    Integer yearsOfExperience
) {}
