package com.kofta.jobapplications;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobApplicationRepository
    extends JpaRepository<JobApplication, Integer>
{
    List<JobApplication> findAllByApplicantId(Integer applicantId);
    List<JobApplication> findAllByPostingId(Integer postingId);

    @EntityGraph(
        attributePaths = {
            "applicant", "posting", "applicant.skills", "posting.skills",
        }
    )
    Optional<JobApplication> findWithDetailsById(Integer id);

    Optional<JobApplication> findByIdAndPosting_CompanyId(
        Integer id,
        Integer companyId
    );

    boolean existsByApplicantIdAndPostingId(
        Integer applicantId,
        Integer postingId
    );
}
