package com.kofta.jobapplications;

import com.kofta.companies.jobpostings.JobPosting;
import com.kofta.softwareengineers.SoftwareEngineer;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;

@Entity
public class JobApplication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "applied_date")
    private LocalDateTime appliedDate;

    private JobApplicationStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "posting_id")
    private JobPosting posting;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "engineer_id")
    private SoftwareEngineer applicant;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDateTime getAppliedDate() {
        return appliedDate;
    }

    public void setAppliedDate(LocalDateTime appliedDate) {
        this.appliedDate = appliedDate;
    }

    public JobApplicationStatus getStatus() {
        return status;
    }

    public void setStatus(JobApplicationStatus status) {
        this.status = status;
    }

    public JobPosting getPosting() {
        return posting;
    }

    public void setPosting(JobPosting posting) {
        this.posting = posting;
    }

    public SoftwareEngineer getApplicant() {
        return applicant;
    }

    public void setApplicant(SoftwareEngineer applicant) {
        this.applicant = applicant;
    }
}
