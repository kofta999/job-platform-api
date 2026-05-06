package com.kofta.softwareengineers;

import com.kofta.engineerprofiles.EngineerProfile;
import com.kofta.engineerprofiles.EngineerProfileRepository;
import com.kofta.errors.ResourceNotFoundException;
import com.kofta.skills.SkillRepository;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SoftwareEngineerService {

    private final SoftwareEngineerRepository softwareEngineerRepository;
    private final SkillRepository skillRepository;
    private final EngineerProfileRepository engineerProfileRepository;

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
                new ResourceNotFoundException(SoftwareEngineer.class, id)
            );
    }

    @Transactional
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
                new ResourceNotFoundException(SoftwareEngineer.class, id)
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

    public void insertEngineerProfile(
        Integer engineerId,
        EngineerProfile profile
    ) {
        var engineer = softwareEngineerRepository
            .findById(engineerId)
            .orElseThrow(() ->
                new ResourceNotFoundException(
                    SoftwareEngineer.class,
                    engineerId
                )
            );

        engineer.setProfile(profile);
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
                new ResourceNotFoundException(
                    SoftwareEngineer.class,
                    engineerId
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
