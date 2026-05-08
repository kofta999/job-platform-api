package com.kofta.softwareengineers;

import com.kofta.engineerprofiles.CreateEngineerProfileDto;
import com.kofta.engineerprofiles.UpdateEngineerProfileDto;
import com.kofta.jobapplications.CreateJobApplicationDto;
import com.kofta.jobapplications.JobApplicationDetailsDto;
import com.kofta.jobapplications.JobApplicationDto;
import com.kofta.jobapplications.JobApplicationMapper;
import com.kofta.jobapplications.JobApplicationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
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
@RequiredArgsConstructor
@Tag(name = "Software Engineers", description = "Engineers and applications")
@SecurityRequirement(name = "bearerAuth")
public class SoftwareEngineerController {

    private final SoftwareEngineerService softwareEngineerService;
    private final JobApplicationService jobApplicationService;
    private final SoftwareEngineerMapper engineerMapper;
    private final JobApplicationMapper jobApplicationMapper;

    @GetMapping
    @Operation(summary = "List engineers", operationId = "listEngineers")
    public Slice<SoftwareEngineerDto> getAll(
        @PageableDefault Pageable pageable,
        @RequestParam(required = false) String skill,
        @RequestParam(required = false) Integer yearsGreaterEqual
    ) {
        return softwareEngineerService
            .getSoftwareEngineers(yearsGreaterEqual, skill, pageable)
            .map(engineerMapper::toDto);
    }

    @GetMapping("{id}")
    @Operation(summary = "Get engineer", operationId = "getEngineer")
    public SoftwareEngineerWithProfileDto getById(@PathVariable Integer id) {
        return engineerMapper.toWithProfileDto(
            softwareEngineerService.getSoftwareEngineerById(id)
        );
    }

    @PreAuthorize("@sec.isSelfEngineer(#id)")
    @PatchMapping("{id}")
    @Operation(summary = "Update engineer", operationId = "updateEngineer")
    public SoftwareEngineerDto update(
        @Valid @RequestBody UpdateSoftwareEngineerDto updated,
        @PathVariable Integer id
    ) {
        var eng = softwareEngineerService.updateSoftwareEngineer(
            id,
            engineerMapper.fromUpdateDto(updated)
        );

        return engineerMapper.toDto(eng);
    }

    @PreAuthorize("@sec.isSelfEngineer(#id)")
    @DeleteMapping("{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete engineer", operationId = "deleteEngineer")
    public void delete(@PathVariable Integer id) {
        softwareEngineerService.deleteSoftwareEngineer(id);
    }

    @PreAuthorize("@sec.isSelfEngineer(#engineerId)")
    @PostMapping("{engineerId}/profile")
    @ResponseStatus(code = HttpStatus.CREATED)
    @Operation(
        summary = "Create profile",
        operationId = "createEngineerProfile"
    )
    public void addNewProfile(
        @PathVariable Integer engineerId,
        @Valid @RequestBody CreateEngineerProfileDto newProfile
    ) {
        softwareEngineerService.insertEngineerProfile(
            engineerId,
            engineerMapper.fromCreateDto(newProfile)
        );
    }

    @PreAuthorize("@sec.isSelfEngineer(#engineerId)")
    @PatchMapping("{engineerId}/profile")
    @Operation(
        summary = "Update profile",
        operationId = "updateEngineerProfile"
    )
    public void updateProfile(
        @PathVariable Integer engineerId,
        @Valid @RequestBody UpdateEngineerProfileDto newProfile
    ) {
        softwareEngineerService.updateEngineerProfile(
            engineerId,
            engineerMapper.fromUpdateDto(newProfile)
        );
    }

    @PreAuthorize("@sec.isSelfEngineer(#engineerId)")
    @PostMapping("{engineerId}/applications")
    @ResponseStatus(code = HttpStatus.CREATED)
    @Operation(
        summary = "Submit application",
        operationId = "submitApplication"
    )
    public JobApplicationDto submitApplication(
        @PathVariable Integer engineerId,
        @RequestBody @Valid CreateJobApplicationDto application
    ) {
        return jobApplicationMapper.toDto(
            jobApplicationService.submitApplication(
                engineerId,
                application.postingId()
            )
        );
    }

    @PreAuthorize("@sec.isSelfEngineer(#engineerId)")
    @GetMapping("{engineerId}/applications")
    @Operation(
        summary = "List applications",
        operationId = "listEngineerApplications"
    )
    public List<JobApplicationDto> getApplications(
        @PathVariable Integer engineerId
    ) {
        return jobApplicationService
            .getApplicationsForEngineer(engineerId)
            .stream()
            .map(jobApplicationMapper::toDto)
            .toList();
    }

    @PreAuthorize("@sec.isSelfEngineer(#engineerId)")
    @GetMapping("{engineerId}/applications/{applicationId}")
    @Operation(
        summary = "Get application details",
        operationId = "getEngineerApplication"
    )
    public JobApplicationDetailsDto getApplication(
        @PathVariable Integer engineerId,
        @PathVariable Integer applicationId
    ) {
        return jobApplicationMapper.toDetailsDto(
            jobApplicationService.getApplicationDetailsForEngineer(
                engineerId,
                applicationId
            )
        );
    }
}
