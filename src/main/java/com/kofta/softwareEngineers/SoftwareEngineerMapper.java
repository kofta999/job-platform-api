package com.kofta.softwareEngineers;

import com.kofta.engineerProfiles.CreateEngineerProfileDTO;
import com.kofta.engineerProfiles.EngineerProfile;
import com.kofta.engineerProfiles.EngineerProfileDTO;
import com.kofta.engineerProfiles.UpdateEngineerProfileDTO;
import com.kofta.skills.Skill;
import com.kofta.skills.SkillDTO;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SoftwareEngineerMapper {
    SoftwareEngineerDTO toDto(SoftwareEngineer entity);
    SoftwareEngineerWithProfileDTO toWithProfileDto(SoftwareEngineer entity);

    @Mapping(target = "skills", ignore = true)
    @Mapping(target = "profile", ignore = true)
    SoftwareEngineer fromCreateDto(CreateSoftwareEngineerDTO dto);

    @Mapping(target = "skills", ignore = true)
    @Mapping(target = "profile", ignore = true)
    SoftwareEngineer fromUpdateDto(UpdateSoftwareEngineerDTO dto);

    SkillDTO toSkillDto(Skill skill);

    EngineerProfileDTO toProfileDto(EngineerProfile profile);

    EngineerProfile fromProfileDto(CreateEngineerProfileDTO dto);
    EngineerProfile fromUpdateProfileDto(UpdateEngineerProfileDTO dto);

    default Set<SkillDTO> mapSkills(Set<Skill> skills) {
        if (skills == null) return null;

        return skills
            .stream()
            .map(this::toSkillDto)
            .collect(Collectors.toSet());
    }
}
