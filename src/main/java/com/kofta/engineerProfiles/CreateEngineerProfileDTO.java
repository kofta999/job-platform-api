package com.kofta.engineerProfiles;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.URL;

public record CreateEngineerProfileDTO(
    @NotBlank @Length(max = 500) String bio,
    @NotBlank
    @URL(host = "github.com", message = "Must be a github.com URL")
    String githubUrl,
    @NotBlank
    @URL(host = "linkedin.com", message = "Must be a linkedin.com URL")
    String linkedinUrl,
    @NotBlank String location
) {}
