package com.kofta.jobapplications;

import com.kofta.common.BaseAuditableEntity;
import com.kofta.companies.jobpostings.JobPosting;
import com.kofta.softwareengineers.SoftwareEngineer;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(
    name = "job_application",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "uc_engineer_posting",
            columnNames = { "posting_id", "engineer_id" }
        ),
    }
)
public class JobApplication extends BaseAuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "applied_date")
    private LocalDateTime appliedDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private JobApplicationStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "posting_id")
    private JobPosting posting;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "engineer_id")
    private SoftwareEngineer applicant;
}
