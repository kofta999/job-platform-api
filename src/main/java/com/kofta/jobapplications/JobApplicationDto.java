package com.kofta.jobapplications;

import java.time.LocalDateTime;

public record JobApplicationDto(
    Integer id,
    LocalDateTime appliedDate,
    JobApplicationStatus status
) {}
