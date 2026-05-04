package com.kofta.companies;

import com.kofta.companies.jobpostings.JobPosting;
import com.kofta.companies.jobpostings.JobPostingRepository;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CompanyService {

    private CompanyRepository companyRepository;
    private JobPostingRepository jobPostingRepository;

    public CompanyService(
        CompanyRepository companyRepository,
        JobPostingRepository jobPostingRepository
    ) {
        this.companyRepository = companyRepository;
        this.jobPostingRepository = jobPostingRepository;
    }

    // Companies

    public Slice<Company> getAllCompanies(Pageable pageable) {
        return companyRepository.findAll(pageable);
    }

    public Company insertCompany(Company company) {
        return companyRepository.save(company);
    }

    public Company updateCompany(Integer companyId, Company updated) {
        // TODO: Add proper error type
        var existing = companyRepository.findById(companyId).orElseThrow();

        if (updated.getName() != null) {
            existing.setName(updated.getName());
        }

        if (updated.getHqLocation() != null) {
            existing.setHqLocation(updated.getHqLocation());
        }

        return companyRepository.save(existing);
    }

    public void deleteCompany(Integer companyId) {
        companyRepository.deleteById(companyId);
    }

    // Job Postings

    @Transactional(readOnly = true)
    public List<JobPosting> getCompanyJobPostings(Integer companyId) {
        var company = companyRepository.findById(companyId).orElseThrow();

        return List.copyOf(company.getJobPostings());
    }

    public JobPosting insertJobPosting(Integer companyId, JobPosting posting) {
        var company = companyRepository.findById(companyId).orElseThrow();

        posting.setCompany(company);

        return jobPostingRepository.save(posting);
    }

    public JobPosting getCompanyJobPosting(
        Integer companyId,
        Integer postingId
    ) {
        var posting = jobPostingRepository.findById(postingId).orElseThrow();

        if (
            posting.getCompany() == null ||
            !posting.getCompany().getId().equals(companyId)
        ) {
            throw new IllegalArgumentException(
                "Job posting does not belong to company"
            );
        }

        return posting;
    }

    public JobPosting updateJobPosting(
        Integer companyId,
        Integer postingId,
        JobPosting updated
    ) {
        var posting = getCompanyJobPosting(companyId, postingId);

        if (updated.getTitle() != null) {
            posting.setTitle(updated.getTitle());
        }

        if (updated.getDescription() != null) {
            posting.setDescription(updated.getDescription());
        }

        if (updated.getSalary() != null) {
            posting.setSalary(updated.getSalary());
        }

        return jobPostingRepository.save(posting);
    }

    public void deleteJobPosting(Integer companyId, Integer postingId) {
        // To check if the company owning the posting exists or not
        var posting = getCompanyJobPosting(companyId, postingId);

        jobPostingRepository.delete(posting);
    }
}
