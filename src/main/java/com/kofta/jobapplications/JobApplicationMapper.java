package com.kofta.jobapplications;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface JobApplicationMapper {
    JobApplicationDto toDto(JobApplication jobApplication);
    JobApplicationDetailsDto toDetailsDto(JobApplication jobApplication);
}
