package com.kofta.softwareEngineers;

import jakarta.validation.Valid;
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
    private final SoftwareEngineerMapper mapper;

    public SoftwareEngineerController(
        SoftwareEngineerService softwareEngineerService,
        SoftwareEngineerMapper softwareEngineerMapper
    ) {
        this.softwareEngineerService = softwareEngineerService;
        this.mapper = softwareEngineerMapper;
    }

    @GetMapping
    public List<SoftwareEngineerDTO> getAll() {
        return softwareEngineerService
            .getSoftwareEngineers()
            .stream()
            .map(mapper::toDto)
            .toList();
    }

    @GetMapping("{id}")
    public SoftwareEngineerDTO getById(@PathVariable Integer id) {
        return mapper.toDto(
            softwareEngineerService.getSoftwareEngineerById(id)
        );
    }

    @PostMapping
    public void addNew(
        @Valid @RequestBody CreateSoftwareEngineerDTO softwareEngineer
    ) {
        softwareEngineerService.insertSoftwareEngineer(
            mapper.fromCreateDto(softwareEngineer)
        );
    }

    @PutMapping("{id}")
    public void update(
        @Valid @RequestBody CreateSoftwareEngineerDTO updated,
        @PathVariable Integer id
    ) {
        softwareEngineerService.updateSoftwareEngineer(
            id,
            mapper.fromCreateDto(updated)
        );
    }

    @DeleteMapping("{id}")
    public void delete(@PathVariable Integer id) {
        softwareEngineerService.deleteSoftwareEngineer(id);
    }
}
