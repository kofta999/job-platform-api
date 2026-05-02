package com.kofta.softwareEngineers;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SoftwareEngineerRepository
    extends JpaRepository<SoftwareEngineer, Integer>
{
    Slice<SoftwareEngineer> findBy(Pageable pageable);
}
