package com.kofta.softwareEngineers;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class SoftwareEngineerService {

    private final SoftwareEngineerRepository softwareEngineerRepository;

    public SoftwareEngineerService(
        SoftwareEngineerRepository softwareEngineerRepository
    ) {
        this.softwareEngineerRepository = softwareEngineerRepository;
    }

    public Slice<SoftwareEngineer> getSoftwareEngineers(
        Integer years,
        String techStack,
        Pageable pageable
    ) {
        Specification<SoftwareEngineer> spec = Specification.unrestricted();

        if (techStack != null) {
            spec = spec.and(
                SoftwareEngineerSpecification.hasTechStack(techStack)
            );
        }

        if (years != null) {
            spec = spec.and(
                SoftwareEngineerSpecification.hasYearsGreaterThanOrEqual(years)
            );
        }

        return softwareEngineerRepository.findAll(spec, pageable);
    }

    public SoftwareEngineer getSoftwareEngineerById(Integer id) {
        return softwareEngineerRepository
            .findById(id)
            .orElseThrow(() ->
                new SoftwareEngineerNotFoundException(
                    "Software Engineer with ID " + id + " is not found"
                )
            );
    }

    public void insertSoftwareEngineer(SoftwareEngineer softwareEngineer) {
        softwareEngineerRepository.save(softwareEngineer);
    }

    public void updateSoftwareEngineer(Integer id, SoftwareEngineer updated) {
        var existing = softwareEngineerRepository
            .findById(id)
            .orElseThrow(() ->
                new SoftwareEngineerNotFoundException(
                    "Software Engineer with ID " + id + " is not found"
                )
            );

        // will be used in a PUT method, so should be expected to have all fields
        existing.setName(updated.getName());
        existing.setTechStack(updated.getTechStack());

        softwareEngineerRepository.save(existing);
    }

    public void deleteSoftwareEngineer(Integer id) {
        if (!softwareEngineerRepository.existsById(id)) {
            throw new SoftwareEngineerNotFoundException(
                "Software Engineer with ID " + id + " is not found"
            );
        }
        softwareEngineerRepository.deleteById(id);
    }
}
