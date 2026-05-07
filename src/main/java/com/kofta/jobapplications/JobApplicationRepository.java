package com.kofta.jobapplications;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobApplicationRepository
    extends JpaRepository<JobApplication, Integer>
{
    List<JobApplication> findAllByApplicantId(Integer applicantId);

    @EntityGraph(
        attributePaths = {
            "applicant", "posting", "applicant.skills", "posting.skills",
        }
    )
    Optional<JobApplication> findWithDetailsById(Integer id);

    boolean existsByApplicantIdAndPostingId(
        Integer applicantId,
        Integer postingId
    );
}
