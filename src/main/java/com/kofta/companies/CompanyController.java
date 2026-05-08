package com.kofta.companies;

import com.kofta.companies.jobpostings.CreateJobPostingDto;
import com.kofta.companies.jobpostings.JobPostingDetailsDto;
import com.kofta.companies.jobpostings.JobPostingItemDto;
import com.kofta.companies.jobpostings.UpdateJobPostingDto;
import com.kofta.jobapplications.JobApplicationDetailsDto;
import com.kofta.jobapplications.JobApplicationDto;
import com.kofta.jobapplications.JobApplicationMapper;
import com.kofta.jobapplications.JobApplicationService;
import com.kofta.jobapplications.UpdateApplicationStatusDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
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
@RequestMapping("companies")
@RequiredArgsConstructor
@Tag(name = "Companies", description = "Company management and job postings")
@SecurityRequirement(name = "bearerAuth")
public class CompanyController {

    private final CompanyService companyService;
    private final JobApplicationService jobApplicationService;
    private final CompanyMapper companyMapper;
    private final JobApplicationMapper jobApplicationMapper;

    @GetMapping
    @Operation(summary = "List companies", operationId = "listCompanies")
    @ApiResponses(
        {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(
                responseCode = "401",
                description = "Unauthorized",
                content = @Content(
                    schema = @Schema(implementation = ProblemDetail.class)
                )
            ),
        }
    )
    public Slice<CompanyDto> getAll(@PageableDefault Pageable pageable) {
        return companyService
            .getAllCompanies(pageable)
            .map(companyMapper::toDto);
    }

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    @Operation(summary = "Create company", operationId = "createCompany")
    @ApiResponses(
        {
            @ApiResponse(responseCode = "201", description = "Created"),
            @ApiResponse(
                responseCode = "400",
                description = "Validation error",
                content = @Content(
                    schema = @Schema(implementation = ProblemDetail.class)
                )
            ),
        }
    )
    public CompanyDto create(@RequestBody @Valid CreateCompanyDto company) {
        return companyMapper.toDto(
            companyService.insertCompany(companyMapper.fromCreateDto(company))
        );
    }

    // TODO: Add GET {id} when you've enough details for it being worth later

    @PreAuthorize("@sec.belongsToCompany(#id)")
    @PatchMapping("{id}")
    @Operation(summary = "Update company", operationId = "updateCompany")
    @ApiResponses(
        {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(
                responseCode = "400",
                description = "Validation error",
                content = @Content(
                    schema = @Schema(implementation = ProblemDetail.class)
                )
            ),
            @ApiResponse(
                responseCode = "403",
                description = "Forbidden",
                content = @Content(
                    schema = @Schema(implementation = ProblemDetail.class)
                )
            ),
            @ApiResponse(
                responseCode = "404",
                description = "Not found",
                content = @Content(
                    schema = @Schema(implementation = ProblemDetail.class)
                )
            ),
        }
    )
    public CompanyDto update(
        @PathVariable Integer id,
        @RequestBody @Valid UpdateCompanyDto company
    ) {
        return companyMapper.toDto(
            companyService.updateCompany(
                id,
                companyMapper.fromUpdateDto(company)
            )
        );
    }

    @PreAuthorize("@sec.belongsToCompany(#id)")
    @DeleteMapping("{id}")
    @Operation(summary = "Delete company", operationId = "deleteCompany")
    @ApiResponses(
        {
            @ApiResponse(responseCode = "204", description = "No Content"),
            @ApiResponse(
                responseCode = "403",
                description = "Forbidden",
                content = @Content(
                    schema = @Schema(implementation = ProblemDetail.class)
                )
            ),
            @ApiResponse(
                responseCode = "404",
                description = "Not found",
                content = @Content(
                    schema = @Schema(implementation = ProblemDetail.class)
                )
            ),
        }
    )
    public void delete(@PathVariable Integer id) {
        companyService.deleteCompany(id);
    }

    @GetMapping("{id}/job-postings")
    @Operation(
        summary = "List job postings",
        operationId = "listCompanyJobPostings"
    )
    @ApiResponses(
        {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(
                responseCode = "404",
                description = "Company not found",
                content = @Content(
                    schema = @Schema(implementation = ProblemDetail.class)
                )
            ),
        }
    )
    public Slice<JobPostingItemDto> getPostings(
        @PathVariable Integer id,
        @PageableDefault Pageable pageable,
        @RequestParam(required = false) String skill,
        @RequestParam(required = false) Integer minSalary,
        @RequestParam(required = false) Integer maxSalary
    ) {
        return companyService
            .getCompanyJobPostings(id, skill, minSalary, maxSalary, pageable)
            .map(companyMapper::toItemDto);
    }

    @PreAuthorize("@sec.belongsToCompany(#id)")
    @PostMapping("{id}/job-postings")
    @ResponseStatus(code = HttpStatus.CREATED)
    @Operation(summary = "Create job posting", operationId = "createJobPosting")
    @ApiResponses(
        {
            @ApiResponse(responseCode = "201", description = "Created"),
            @ApiResponse(
                responseCode = "400",
                description = "Validation error",
                content = @Content(
                    schema = @Schema(implementation = ProblemDetail.class)
                )
            ),
            @ApiResponse(
                responseCode = "403",
                description = "Forbidden",
                content = @Content(
                    schema = @Schema(implementation = ProblemDetail.class)
                )
            ),
            @ApiResponse(
                responseCode = "404",
                description = "Company not found",
                content = @Content(
                    schema = @Schema(implementation = ProblemDetail.class)
                )
            ),
        }
    )
    public JobPostingDetailsDto createPosting(
        @PathVariable Integer id,
        @RequestBody CreateJobPostingDto posting
    ) {
        return companyMapper.toDetailsDto(
            companyService.insertJobPosting(
                id,
                companyMapper.fromCreateDto(posting),
                posting.skillIds()
            )
        );
    }

    @GetMapping("{companyId}/job-postings/{postingId}")
    @Operation(summary = "Get job posting", operationId = "getJobPosting")
    @ApiResponses(
        {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(
                responseCode = "404",
                description = "Not found",
                content = @Content(
                    schema = @Schema(implementation = ProblemDetail.class)
                )
            ),
        }
    )
    public JobPostingDetailsDto getPosting(
        @PathVariable Integer companyId,
        @PathVariable Integer postingId
    ) {
        return companyMapper.toDetailsDto(
            companyService.getCompanyJobPosting(companyId, postingId)
        );
    }

    @PreAuthorize("@sec.belongsToCompany(#companyId)")
    @PatchMapping("{companyId}/job-postings/{postingId}")
    @Operation(summary = "Update job posting", operationId = "updateJobPosting")
    @ApiResponses(
        {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(
                responseCode = "400",
                description = "Validation error",
                content = @Content(
                    schema = @Schema(implementation = ProblemDetail.class)
                )
            ),
            @ApiResponse(
                responseCode = "403",
                description = "Forbidden",
                content = @Content(
                    schema = @Schema(implementation = ProblemDetail.class)
                )
            ),
            @ApiResponse(
                responseCode = "404",
                description = "Not found",
                content = @Content(
                    schema = @Schema(implementation = ProblemDetail.class)
                )
            ),
        }
    )
    public JobPostingDetailsDto updatePosting(
        @PathVariable Integer companyId,
        @PathVariable Integer postingId,
        @RequestBody UpdateJobPostingDto posting
    ) {
        return companyMapper.toDetailsDto(
            companyService.updateJobPosting(
                companyId,
                postingId,
                companyMapper.fromUpdateDto(posting),
                posting.skillIds()
            )
        );
    }

    @PreAuthorize("@sec.belongsToCompany(#companyId)")
    @DeleteMapping("{companyId}/job-postings/{postingId}")
    @Operation(summary = "Delete job posting", operationId = "deleteJobPosting")
    @ApiResponses(
        {
            @ApiResponse(responseCode = "204", description = "No Content"),
            @ApiResponse(
                responseCode = "403",
                description = "Forbidden",
                content = @Content(
                    schema = @Schema(implementation = ProblemDetail.class)
                )
            ),
            @ApiResponse(
                responseCode = "404",
                description = "Not found",
                content = @Content(
                    schema = @Schema(implementation = ProblemDetail.class)
                )
            ),
        }
    )
    public void deletePosting(
        @PathVariable Integer companyId,
        @PathVariable Integer postingId
    ) {
        companyService.deleteJobPosting(companyId, postingId);
    }

    @PreAuthorize("@sec.belongsToCompany(#companyId)")
    @GetMapping("{companyId}/job-postings/{postingId}/applications")
    @Operation(
        summary = "List job applications",
        operationId = "listJobApplications"
    )
    @ApiResponses(
        {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(
                responseCode = "403",
                description = "Forbidden",
                content = @Content(
                    schema = @Schema(implementation = ProblemDetail.class)
                )
            ),
            @ApiResponse(
                responseCode = "404",
                description = "Posting not found",
                content = @Content(
                    schema = @Schema(implementation = ProblemDetail.class)
                )
            ),
        }
    )
    public List<JobApplicationDto> getApplications(
        @PathVariable Integer companyId,
        @PathVariable Integer postingId
    ) {
        return jobApplicationService
            .getApplicationsForPosting(companyId, postingId)
            .stream()
            .map(jobApplicationMapper::toDto)
            .toList();
    }

    @PreAuthorize("@sec.belongsToCompany(#companyId)")
    @GetMapping(
        "{companyId}/job-postings/{postingId}/applications/{applicationId}"
    )
    @Operation(
        summary = "Get job application details",
        operationId = "getJobApplicationDetails"
    )
    @ApiResponses(
        {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(
                responseCode = "403",
                description = "Forbidden",
                content = @Content(
                    schema = @Schema(implementation = ProblemDetail.class)
                )
            ),
            @ApiResponse(
                responseCode = "404",
                description = "Not found",
                content = @Content(
                    schema = @Schema(implementation = ProblemDetail.class)
                )
            ),
        }
    )
    public JobApplicationDetailsDto getApplicationDetails(
        @PathVariable Integer companyId,
        @PathVariable Integer postingId,
        @PathVariable Integer applicationId
    ) {
        return jobApplicationMapper.toDetailsDto(
            jobApplicationService.getApplicationDetailsForCompany(
                companyId,
                applicationId
            )
        );
    }

    @PreAuthorize("@sec.belongsToCompany(#companyId)")
    @PatchMapping("{companyId}/applications/{applicationId}/status")
    @Operation(
        summary = "Update application status",
        operationId = "updateApplicationStatus"
    )
    @ApiResponses(
        {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(
                responseCode = "403",
                description = "Forbidden",
                content = @Content(
                    schema = @Schema(implementation = ProblemDetail.class)
                )
            ),
            @ApiResponse(
                responseCode = "404",
                description = "Not found",
                content = @Content(
                    schema = @Schema(implementation = ProblemDetail.class)
                )
            ),
            @ApiResponse(
                responseCode = "422",
                description = "Invalid status transition",
                content = @Content(
                    schema = @Schema(implementation = ProblemDetail.class)
                )
            ),
        }
    )
    public JobApplicationDto updateStatus(
        @PathVariable Integer companyId,
        @PathVariable Integer applicationId,
        @RequestBody @Valid UpdateApplicationStatusDto request
    ) {
        return jobApplicationMapper.toDto(
            jobApplicationService.updateApplicationStatus(
                applicationId,
                companyId,
                request.newStatus()
            )
        );
    }
}
