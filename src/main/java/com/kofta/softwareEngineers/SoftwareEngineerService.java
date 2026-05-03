package com.kofta.softwareEngineers;

import com.kofta.skills.SkillRepository;
import java.util.Set;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class SoftwareEngineerService {

    private final SoftwareEngineerRepository softwareEngineerRepository;
    private final SkillRepository skillRepository;

    public SoftwareEngineerService(
        SoftwareEngineerRepository softwareEngineerRepository,
        SkillRepository skillRepository
    ) {
        this.softwareEngineerRepository = softwareEngineerRepository;
        this.skillRepository = skillRepository;
    }

    public Slice<SoftwareEngineer> getSoftwareEngineers(
        Integer years,
        String skill,
        Pageable pageable
    ) {
        Specification<SoftwareEngineer> spec = Specification.unrestricted();

        if (skill != null) {
            spec = spec.and(SoftwareEngineerSpecification.hasSkill(skill));
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

    public void insertSoftwareEngineer(
        SoftwareEngineer softwareEngineer,
        Set<Integer> skillIds
    ) {
        var skills = skillRepository.findAllById(skillIds);

        skills.forEach(softwareEngineer::addSkill);

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
        // TODO: Add rest
        existing.setName(updated.getName());
        existing.setYearsOfExperience(updated.getYearsOfExperience());

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
