package com.kofta;

import java.util.List;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("software-engineers")
public class SoftwareEngineerController {

    private final SoftwareEngineerService softwareEngineerService;

    public SoftwareEngineerController(
        SoftwareEngineerService softwareEngineerService
    ) {
        this.softwareEngineerService = softwareEngineerService;
    }

    @GetMapping
    public List<SoftwareEngineer> getAll() {
        return softwareEngineerService.getSoftwareEngineers();
    }

    @GetMapping("{id}")
    public SoftwareEngineer getById(@PathVariable Integer id) {
        return softwareEngineerService.getSoftwareEngineerById(id);
    }

    @PostMapping
    public void addNew(@RequestBody SoftwareEngineer softwareEngineer) {
        softwareEngineerService.insertSoftwareEngineer(softwareEngineer);
    }

    @PutMapping("{id}")
    public void update(
        @RequestBody SoftwareEngineer updated,
        @PathVariable Integer id
    ) {
        softwareEngineerService.updateSoftwareEngineer(id, updated);
    }

    @DeleteMapping("{id}")
    public void delete(@PathVariable Integer id) {
        softwareEngineerService.deleteSoftwareEngineer(id);
    }
}
