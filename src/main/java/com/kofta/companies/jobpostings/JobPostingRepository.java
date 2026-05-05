package com.kofta.companies.jobpostings;

import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobPostingRepository
    extends JpaRepository<JobPosting, Integer>
{
    @EntityGraph(attributePaths = { "skills" })
    Optional<JobPosting> findWithSkillsById(Integer id);
}
