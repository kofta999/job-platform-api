package com.kofta.softwareEngineers;

import com.kofta.engineerProfiles.EngineerProfile;
import com.kofta.engineerProfiles.EngineerProfileRepository;
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
    private final EngineerProfileRepository engineerProfileRepository;

    public SoftwareEngineerService(
        SoftwareEngineerRepository softwareEngineerRepository,
        SkillRepository skillRepository,
        EngineerProfileRepository engineerProfileRepository
    ) {
        this.softwareEngineerRepository = softwareEngineerRepository;
        this.skillRepository = skillRepository;
        this.engineerProfileRepository = engineerProfileRepository;
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

    public void insertEngineerProfile(
        Integer engineerId,
        EngineerProfile profile
    ) {
        var engineer = softwareEngineerRepository
            .findById(engineerId)
            .orElseThrow(() ->
                new SoftwareEngineerNotFoundException(
                    "Software Engineer with ID " + engineerId + " is not found"
                )
            );

        engineer.setProfile(profile);
        engineerProfileRepository.save(profile);
        softwareEngineerRepository.save(engineer);
    }

    public void updateEngineerProfile(
        Integer engineerId,
        EngineerProfile updated
    ) {
        var engineer = softwareEngineerRepository
            .findById(engineerId)
            .orElseThrow(() ->
                new SoftwareEngineerNotFoundException(
                    "Software Engineer with ID " + engineerId + " is not found"
                )
            );

        var profile = engineer.getProfile();

        if (updated.getBio() != null) {
            profile.setBio(updated.getBio());
        }

        if (updated.getGithubUrl() != null) {
            profile.setGithubUrl(updated.getGithubUrl());
        }

        if (updated.getLinkedinUrl() != null) {
            profile.setLinkedinUrl(updated.getLinkedinUrl());
        }

        if (updated.getLocation() != null) {
            profile.setLocation(updated.getLocation());
        }

        engineerProfileRepository.save(profile);
    }
}
