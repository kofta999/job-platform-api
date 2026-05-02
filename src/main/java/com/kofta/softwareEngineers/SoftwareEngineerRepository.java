package com.kofta.softwareEngineers;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface SoftwareEngineerRepository
    extends
        JpaRepository<SoftwareEngineer, Integer>,
        JpaSpecificationExecutor<SoftwareEngineer>
{
    Slice<SoftwareEngineer> findBy(Pageable pageable);
}
