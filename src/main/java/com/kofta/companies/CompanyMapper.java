package com.kofta.companies;

import com.kofta.companies.jobpostings.CreateJobPostingDto;
import com.kofta.companies.jobpostings.JobPosting;
import com.kofta.companies.jobpostings.JobPostingDetailsDto;
import com.kofta.companies.jobpostings.JobPostingItemDto;
import com.kofta.companies.jobpostings.UpdateJobPostingDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CompanyMapper {
    // Companies
    CompanyDto toDto(Company source);
    Company fromCreateDto(CreateCompanyDto source);
    Company fromUpdateDto(UpdateCompanyDto source);

    // Postings
    JobPostingItemDto toItemDto(JobPosting source);
    JobPostingDetailsDto toDetailsDto(JobPosting source);

    @Mapping(target = "skills", ignore = true)
    JobPosting fromCreateDto(CreateJobPostingDto source);

    @Mapping(target = "skills", ignore = true)
    JobPosting fromUpdateDto(UpdateJobPostingDto source);
}
