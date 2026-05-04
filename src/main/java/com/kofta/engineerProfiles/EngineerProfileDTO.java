package com.kofta.engineerProfiles;

public record EngineerProfileDTO(
    Integer id,
    String bio,
    String githubUrl,
    String linkedinUrl,
    String location
) {}
