package com.kofta.engineerprofiles;

public record EngineerProfileDto(
    Integer id,
    String bio,
    String githubUrl,
    String linkedinUrl,
    String location
) {}
