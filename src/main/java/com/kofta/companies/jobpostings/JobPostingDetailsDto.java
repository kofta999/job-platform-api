package com.kofta.companies.jobpostings;

public record JobPostingDetailsDto(
    Integer id,
    String title,
    String description,
    Integer salary
) {}
