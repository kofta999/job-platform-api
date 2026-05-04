package com.kofta.softwareEngineers;

import com.kofta.engineerProfiles.CreateEngineerProfileDTO;
import com.kofta.engineerProfiles.UpdateEngineerProfileDTO;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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
    public SoftwareEngineerWithProfileDTO getById(@PathVariable Integer id) {
        return mapper.toWithProfileDto(
            softwareEngineerService.getSoftwareEngineerById(id)
        );
    }

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public SoftwareEngineerDTO addNew(
        @Valid @RequestBody CreateSoftwareEngineerDTO softwareEngineer
    ) {
        var eng = softwareEngineerService.insertSoftwareEngineer(
            mapper.fromCreateDto(softwareEngineer),
            softwareEngineer.skillIds()
        );

        return mapper.toDto(eng);
    }

    @PatchMapping("{id}")
    public SoftwareEngineerDTO update(
        @Valid @RequestBody UpdateSoftwareEngineerDTO updated,
        @PathVariable Integer id
    ) {
        var eng = softwareEngineerService.updateSoftwareEngineer(
            id,
            mapper.fromUpdateDto(updated)
        );

        return mapper.toDto(eng);
    }

    @DeleteMapping("{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer id) {
        softwareEngineerService.deleteSoftwareEngineer(id);
    }

    @PostMapping("{engineerId}/profile")
    @ResponseStatus(code = HttpStatus.CREATED)
    public void addNewProfile(
        @PathVariable Integer engineerId,
        @Valid @RequestBody CreateEngineerProfileDTO newProfile
    ) {
        softwareEngineerService.insertEngineerProfile(
            engineerId,
            mapper.fromProfileDto(newProfile)
        );
    }

    @PatchMapping("{engineerId}/profile")
    public void updateProfile(
        @PathVariable Integer engineerId,
        @Valid @RequestBody UpdateEngineerProfileDTO newProfile
    ) {
        softwareEngineerService.updateEngineerProfile(
            engineerId,
            mapper.fromUpdateProfileDto(newProfile)
        );
    }
}
