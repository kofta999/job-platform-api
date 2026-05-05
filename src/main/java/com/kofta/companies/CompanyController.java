package com.kofta.companies;

import com.kofta.companies.jobpostings.CreateJobPostingDto;
import com.kofta.companies.jobpostings.JobPostingDetailsDto;
import com.kofta.companies.jobpostings.JobPostingItemDto;
import com.kofta.companies.jobpostings.UpdateJobPostingDto;
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
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("companies")
public class CompanyController {

    private CompanyService companyService;
    private CompanyMapper mapper;

    public CompanyController(
        CompanyService companyService,
        CompanyMapper mapper
    ) {
        this.companyService = companyService;
        this.mapper = mapper;
    }

    @GetMapping
    public Slice<CompanyDto> getAll(@PageableDefault Pageable pageable) {
        return companyService.getAllCompanies(pageable).map(mapper::toDto);
    }

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public CompanyDto create(@RequestBody @Valid CreateCompanyDto company) {
        return mapper.toDto(
            companyService.insertCompany(mapper.fromCreateDto(company))
        );
    }

    // TODO: Add GET {id} when you've enough details for it being worth later
    @PatchMapping("{id}")
    public CompanyDto update(
        @PathVariable Integer id,
        @RequestBody @Valid UpdateCompanyDto company
    ) {
        return mapper.toDto(
            companyService.updateCompany(id, mapper.fromUpdateDto(company))
        );
    }

    @DeleteMapping("{id}")
    public void delete(@PathVariable Integer id) {
        companyService.deleteCompany(id);
    }

    @GetMapping("{id}/job-postings")
    public List<JobPostingItemDto> getPostings(@PathVariable Integer id) {
        return companyService
            .getCompanyJobPostings(id)
            .stream()
            .map(mapper::toItemDto)
            .toList();
    }

    @PostMapping("{id}/job-postings")
    @ResponseStatus(code = HttpStatus.CREATED)
    public JobPostingDetailsDto createPosting(
        @PathVariable Integer id,
        @RequestBody CreateJobPostingDto posting
    ) {
        return mapper.toDetailsDto(
            companyService.insertJobPosting(id, mapper.fromCreateDto(posting))
        );
    }

    @GetMapping("{companyId}/job-postings/{postingId}")
    public JobPostingDetailsDto getPosting(
        @PathVariable Integer companyId,
        @PathVariable Integer postingId
    ) {
        return mapper.toDetailsDto(
            companyService.getCompanyJobPosting(companyId, postingId)
        );
    }

    @PatchMapping("{companyId}/job-postings/{postingId}")
    public JobPostingDetailsDto updatePosting(
        @PathVariable Integer companyId,
        @PathVariable Integer postingId,
        @RequestBody UpdateJobPostingDto posting
    ) {
        return mapper.toDetailsDto(
            companyService.updateJobPosting(
                companyId,
                postingId,
                mapper.fromUpdateDto(posting)
            )
        );
    }

    @DeleteMapping("{companyId}/job-postings/{postingId}")
    public void deletePosting(
        @PathVariable Integer companyId,
        @PathVariable Integer postingId
    ) {
        companyService.deleteJobPosting(companyId, postingId);
    }
}
