package com.kofta.companies;

import com.kofta.companies.jobpostings.CreateJobPostingDto;
import com.kofta.companies.jobpostings.JobPosting;
import com.kofta.companies.jobpostings.JobPostingDetailsDto;
import com.kofta.companies.jobpostings.JobPostingItemDto;
import com.kofta.companies.jobpostings.UpdateJobPostingDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CompanyMapper {
    // Companies
    CompanyDto toDto(Company source);
    Company fromCreateDto(CreateCompanyDto source);
    Company fromUpdateDto(UpdateCompanyDto source);

    // Postings
    JobPostingItemDto toItemDto(JobPosting source);
    JobPostingDetailsDto toDetailsDto(JobPosting source);
    JobPosting fromCreateDto(CreateJobPostingDto source);
    JobPosting fromUpdateDto(UpdateJobPostingDto source);
}
