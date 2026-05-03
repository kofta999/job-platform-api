package com.kofta.softwareEngineers;

import java.util.Set;

public record SoftwareEngineerDTO(
    Integer id,
    String name,
    Set<SkillDTO> skills,
    ProfileDTO profile,
    Integer yearsOfExperience
) {
    record SkillDTO(Integer id, String name) {}

    record ProfileDTO(
        Integer id,
        String bio,
        String githubUrl,
        String linkedinUrl,
        String location
    ) {}
}
