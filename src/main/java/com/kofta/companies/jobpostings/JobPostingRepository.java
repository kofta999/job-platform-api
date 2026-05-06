package com.kofta.companies.jobpostings;

import java.util.Optional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobPostingRepository
    extends JpaRepository<JobPosting, Integer>
{
    @EntityGraph(attributePaths = { "skills" })
    Optional<JobPosting> findWithSkillsById(Integer id);

    Slice<JobPosting> findAll(
        Specification<JobPosting> spec,
        Pageable pageable
    );
}
