package com.kofta.softwareEngineers;

import com.kofta.skills.Skill;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SoftwareEngineerMapper {
    SoftwareEngineerDTO toDto(SoftwareEngineer entity);

    @Mapping(target = "skills", ignore = true)
    SoftwareEngineer fromCreateDto(CreateSoftwareEngineerDTO entity);

    SoftwareEngineerDTO.SkillDTO toSkillDto(Skill skill);

    default Set<SoftwareEngineerDTO.SkillDTO> mapSkills(Set<Skill> skills) {
        if (skills == null) return null;

        return skills
            .stream()
            .map(this::toSkillDto)
            .collect(Collectors.toSet());
    }
}
