package com.kofta.engineerprofiles;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.URL;

public record UpdateEngineerProfileDto(
    @Length(max = 500) String bio,
    @URL(host = "github.com", message = "Must be a github.com URL")
    String githubUrl,
    @URL(host = "linkedin.com", message = "Must be a linkedin.com URL")
    String linkedinUrl,
    String location
) {}
