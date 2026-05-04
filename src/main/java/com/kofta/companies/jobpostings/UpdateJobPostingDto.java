package com.kofta.companies.jobpostings;

public record UpdateJobPostingDto(
    String title,
    String description,
    Integer salary
) {}
