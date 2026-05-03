package com.kofta.softwareEngineers;

import java.util.Set;

public record SoftwareEngineerDTO(
    Integer id,
    String name,
    Set<SkillDTO> skills,
    Integer yearsOfExperience
) {
    record SkillDTO(Integer id, String name) {}
}
