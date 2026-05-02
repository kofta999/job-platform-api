package com.kofta.softwareEngineers;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface SoftwareEngineerRepository
    extends
        JpaRepository<SoftwareEngineer, Integer>,
        JpaSpecificationExecutor<SoftwareEngineer> {}
