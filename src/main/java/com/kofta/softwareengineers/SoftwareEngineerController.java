package com.kofta.softwareengineers;

import com.kofta.engineerprofiles.CreateEngineerProfileDto;
import com.kofta.engineerprofiles.UpdateEngineerProfileDto;
import com.kofta.jobapplications.CreateJobApplicationDto;
import com.kofta.jobapplications.JobApplicationDetailsDto;
import com.kofta.jobapplications.JobApplicationDto;
import com.kofta.jobapplications.JobApplicationService;
import jakarta.validation.Valid;
import java.util.List;
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
    private final JobApplicationService jobApplicationService;
    private final SoftwareEngineerMapper mapper;

    public SoftwareEngineerController(
        SoftwareEngineerService softwareEngineerService,
        SoftwareEngineerMapper softwareEngineerMapper,
        JobApplicationService jobApplicationService
    ) {
        this.softwareEngineerService = softwareEngineerService;
        this.mapper = softwareEngineerMapper;
        this.jobApplicationService = jobApplicationService;
    }

    @GetMapping
    public Slice<SoftwareEngineerDto> getAll(
        @PageableDefault Pageable pageable,
        @RequestParam(required = false) String skill,
        @RequestParam(required = false) Integer yearsGreaterEqual
    ) {
        return softwareEngineerService
            .getSoftwareEngineers(yearsGreaterEqual, skill, pageable)
            .map(mapper::toDto);
    }

    @GetMapping("{id}")
    public SoftwareEngineerWithProfileDto getById(@PathVariable Integer id) {
        return mapper.toWithProfileDto(
            softwareEngineerService.getSoftwareEngineerById(id)
        );
    }

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public SoftwareEngineerDto addNew(
        @Valid @RequestBody CreateSoftwareEngineerDto softwareEngineer
    ) {
        var eng = softwareEngineerService.insertSoftwareEngineer(
            mapper.fromCreateDto(softwareEngineer),
            softwareEngineer.skillIds()
        );

        return mapper.toDto(eng);
    }

    @PatchMapping("{id}")
    public SoftwareEngineerDto update(
        @Valid @RequestBody UpdateSoftwareEngineerDto updated,
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
        @Valid @RequestBody CreateEngineerProfileDto newProfile
    ) {
        softwareEngineerService.insertEngineerProfile(
            engineerId,
            mapper.fromCreateDto(newProfile)
        );
    }

    @PatchMapping("{engineerId}/profile")
    public void updateProfile(
        @PathVariable Integer engineerId,
        @Valid @RequestBody UpdateEngineerProfileDto newProfile
    ) {
        softwareEngineerService.updateEngineerProfile(
            engineerId,
            mapper.fromUpdateDto(newProfile)
        );
    }

    @PostMapping("{engineerId}/applications")
    @ResponseStatus(code = HttpStatus.CREATED)
    public JobApplicationDto submitApplication(
        @PathVariable Integer engineerId,
        @RequestBody @Valid CreateJobApplicationDto application
    ) {
        return mapper.toDto(
            jobApplicationService.submitApplication(
                engineerId,
                application.postingId()
            )
        );
    }

    @GetMapping("{engineerId}/applications")
    public List<JobApplicationDto> getApplications(
        @PathVariable Integer engineerId
    ) {
        return jobApplicationService
            .getApplicationsForEngineer(engineerId)
            .stream()
            .map(mapper::toDto)
            .toList();
    }

    @GetMapping("{engineerId}/applications/{applicationId}")
    public JobApplicationDetailsDto getApplication(
        @PathVariable Integer engineerId,
        @PathVariable Integer applicationId
    ) {
        return mapper.toDetailsDto(
            jobApplicationService.getApplicationDetails(
                engineerId,
                applicationId
            )
        );
    }
}
