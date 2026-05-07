package com.kofta.jobapplications;

import com.kofta.companies.jobpostings.JobPosting;
import com.kofta.companies.jobpostings.JobPostingRepository;
import com.kofta.errors.ResourceNotFoundException;
import com.kofta.softwareengineers.SoftwareEngineer;
import com.kofta.softwareengineers.SoftwareEngineerRepository;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JobApplicationService {

    private final JobApplicationRepository jobApplicationRepository;
    private final SoftwareEngineerRepository softwareEngineerRepository;
    private final JobPostingRepository jobPostingRepository;

    public JobApplication submitApplication(
        Integer engineerId,
        Integer postingId
    ) {
        // Validate engineer and application
        if (!softwareEngineerRepository.existsById(engineerId)) {
            throw new ResourceNotFoundException(
                SoftwareEngineer.class,
                engineerId
            );
        }

        if (!jobPostingRepository.existsById(postingId)) {
            throw new ResourceNotFoundException(JobPosting.class, postingId);
        }

        var engineerRef = softwareEngineerRepository.getReferenceById(
            engineerId
        );
        var postingRef = jobPostingRepository.getReferenceById(postingId);

        var application = new JobApplication();

        application.setApplicant(engineerRef);
        application.setPosting(postingRef);
        application.setAppliedDate(LocalDateTime.now());
        application.setStatus(JobApplicationStatus.SENT);

        return jobApplicationRepository.save(application);
    }

    public List<JobApplication> getApplicationsForEngineer(Integer engineerId) {
        return jobApplicationRepository.findAllByApplicantId(engineerId);
    }

    public JobApplication getApplicationDetails(
        Integer engineerId,
        Integer applicationId
    ) {
        var application = jobApplicationRepository
            .findWithDetailsById(applicationId)
            .orElseThrow(() ->
                new ResourceNotFoundException(
                    JobApplication.class,
                    applicationId
                )
            );

        if (!application.getApplicant().getId().equals(engineerId)) {
            throw new ResourceNotFoundException(
                JobApplication.class,
                applicationId
            );
        }

        return application;
    }
}
