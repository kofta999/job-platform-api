package com.kofta.softwareEngineers;

import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
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
    public Slice<SoftwareEngineerDTO> getAll(
        @PageableDefault Pageable pageable,
        @RequestParam(required = false) String skill,
        @RequestParam(required = false) Integer yearsGreaterEqual
    ) {
        return softwareEngineerService
            .getSoftwareEngineers(yearsGreaterEqual, skill, pageable)
            .map(mapper::toDto);
    }

    @GetMapping("{id}")
    public SoftwareEngineerDTO getById(@PathVariable Integer id) {
        return mapper.toDto(
            softwareEngineerService.getSoftwareEngineerById(id)
        );
    }

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public void addNew(
        @Valid @RequestBody CreateSoftwareEngineerDTO softwareEngineer
    ) {
        softwareEngineerService.insertSoftwareEngineer(
            mapper.fromCreateDto(softwareEngineer),
            softwareEngineer.skillIds()
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
