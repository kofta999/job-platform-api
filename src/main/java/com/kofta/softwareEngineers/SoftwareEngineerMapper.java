package com.kofta.softwareEngineers;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SoftwareEngineerMapper {
    SoftwareEngineerDTO toDto(SoftwareEngineer entity);
    SoftwareEngineer fromCreateDto(CreateSoftwareEngineerDTO entity);
}
