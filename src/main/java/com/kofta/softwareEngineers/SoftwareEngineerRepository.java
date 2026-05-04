package com.kofta.softwareEngineers;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface SoftwareEngineerRepository
    extends
        JpaRepository<SoftwareEngineer, Integer>,
        JpaSpecificationExecutor<SoftwareEngineer>
{
    @EntityGraph(attributePaths = "skills")
    Page<SoftwareEngineer> findAll(
        Specification<SoftwareEngineer> spec,
        Pageable pageable
    );

    @EntityGraph(attributePaths = { "skills", "profile" })
    Optional<SoftwareEngineer> findById(Integer id);
}
