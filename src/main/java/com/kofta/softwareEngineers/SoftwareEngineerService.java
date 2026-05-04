package com.kofta.softwareEngineers;

import com.kofta.engineerProfiles.EngineerProfile;
import com.kofta.engineerProfiles.EngineerProfileRepository;
import com.kofta.skills.SkillRepository;
import java.util.Set;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    public SoftwareEngineer insertSoftwareEngineer(
        SoftwareEngineer softwareEngineer,
        Set<Integer> skillIds
    ) {
        var skills = skillRepository.findAllById(skillIds);

        skills.forEach(softwareEngineer::addSkill);

        return softwareEngineerRepository.save(softwareEngineer);
    }

    public SoftwareEngineer updateSoftwareEngineer(
        Integer id,
        SoftwareEngineer updated
    ) {
        var existing = softwareEngineerRepository
            .findById(id)
            .orElseThrow(() ->
                new SoftwareEngineerNotFoundException(
                    "Software Engineer with ID " + id + " is not found"
                )
            );

        if (updated.getName() != null) {
            existing.setName(updated.getName());
        }

        if (updated.getYearsOfExperience() != null) {
            existing.setYearsOfExperience(updated.getYearsOfExperience());
        }
        if (updated.getSkills() != null) {
            existing.setSkills(updated.getSkills());
        }

        return softwareEngineerRepository.save(existing);
    }

    public void deleteSoftwareEngineer(Integer id) {
        softwareEngineerRepository.deleteById(id);
    }

    @Transactional
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

    @Transactional
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
