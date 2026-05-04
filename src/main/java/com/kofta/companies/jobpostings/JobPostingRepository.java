package com.kofta.companies.jobpostings;

import org.springframework.data.jpa.repository.JpaRepository;

public interface JobPostingRepository
    extends JpaRepository<JobPosting, Integer> {}
