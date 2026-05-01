package com.kofta.softwareEngineers;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface SoftwareEngineerMapper {
    SoftwareEngineerDTO toDto(SoftwareEngineer entity);
    SoftwareEngineer fromCreateDto(CreateSoftwareEngineerDTO entity);

    void onUpdate(
        CreateSoftwareEngineerDTO entity,
        @MappingTarget SoftwareEngineer existingEntity
    );
}
