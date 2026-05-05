package com.kofta.softwareengineers;

import com.kofta.engineerprofiles.CreateEngineerProfileDto;
import com.kofta.engineerprofiles.EngineerProfile;
import com.kofta.engineerprofiles.EngineerProfileDto;
import com.kofta.engineerprofiles.UpdateEngineerProfileDto;
import com.kofta.jobapplications.JobApplication;
import com.kofta.jobapplications.JobApplicationDetailsDto;
import com.kofta.jobapplications.JobApplicationDto;
import com.kofta.skills.Skill;
import com.kofta.skills.SkillDto;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SoftwareEngineerMapper {
    // Software Engineer

    SoftwareEngineerDto toDto(SoftwareEngineer entity);
    SoftwareEngineerWithProfileDto toWithProfileDto(SoftwareEngineer entity);

    @Mapping(target = "skills", ignore = true)
    @Mapping(target = "profile", ignore = true)
    SoftwareEngineer fromCreateDto(CreateSoftwareEngineerDto dto);

    @Mapping(target = "skills", ignore = true)
    @Mapping(target = "profile", ignore = true)
    SoftwareEngineer fromUpdateDto(UpdateSoftwareEngineerDto dto);

    // Skills

    SkillDto toSkillDto(Skill skill);

    // Profile
    EngineerProfileDto toDto(EngineerProfile profile);
    EngineerProfile fromCreateDto(CreateEngineerProfileDto dto);
    EngineerProfile fromUpdateDto(UpdateEngineerProfileDto dto);

    default Set<SkillDto> mapSkills(Set<Skill> skills) {
        if (skills == null) return null;

        return skills
            .stream()
            .map(this::toSkillDto)
            .collect(Collectors.toSet());
    }

    // Job Applications

    JobApplicationDto toDto(JobApplication jobApplication);
    JobApplicationDetailsDto toDetailsDto(JobApplication jobApplication);
}
