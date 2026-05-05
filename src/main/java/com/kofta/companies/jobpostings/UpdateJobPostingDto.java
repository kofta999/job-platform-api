package com.kofta.companies.jobpostings;

import java.util.List;

public record UpdateJobPostingDto(
    String title,
    String description,
    Integer salary,
    List<Integer> skillIds
) {}
