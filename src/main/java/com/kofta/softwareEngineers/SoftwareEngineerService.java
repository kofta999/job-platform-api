package com.kofta.softwareEngineers;

import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class SoftwareEngineerService {

    private final SoftwareEngineerRepository softwareEngineerRepository;

    public SoftwareEngineerService(
        SoftwareEngineerRepository softwareEngineerRepository
    ) {
        this.softwareEngineerRepository = softwareEngineerRepository;
    }

    public List<SoftwareEngineer> getSoftwareEngineers() {
        return softwareEngineerRepository.findAll();
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
        softwareEngineerRepository
            .findById(id)
            .orElseThrow(() ->
                new SoftwareEngineerNotFoundException(
                    "Software Engineer with ID " + id + " is not found"
                )
            );
        softwareEngineerRepository.deleteById(id);
    }
}
