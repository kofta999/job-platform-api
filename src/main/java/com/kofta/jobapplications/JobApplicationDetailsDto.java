package com.kofta.jobapplications;

import com.kofta.companies.jobpostings.JobPostingDetailsDto;
import com.kofta.softwareengineers.SoftwareEngineerDto;
import java.time.LocalDateTime;

public record JobApplicationDetailsDto(
    Integer id,
    LocalDateTime appliedDate,
    JobApplicationStatus status,
    SoftwareEngineerDto applicant,
    JobPostingDetailsDto posting
) {}
