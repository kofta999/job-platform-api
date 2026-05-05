package com.kofta.companies.jobpostings;

import com.kofta.skills.Skill;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;

public class JobPostingSpecification {

    public static Specification<JobPosting> hasSkill(String skill) {
        return (root, query, cb) -> {
            Join<JobPosting, Skill> join = root.join("skills");
            query.distinct(true);
            return cb.equal(cb.lower(join.get("name")), skill.toLowerCase());
        };
    }

    /** Minimum salary */
    public static Specification<JobPosting> hasSalaryMoreThan(Integer salary) {
        return (root, query, cb) -> cb.ge(root.get("salary"), salary);
    }

    /** Maximum salary */
    public static Specification<JobPosting> hasSalaryLessThan(Integer salary) {
        return (root, query, cb) -> cb.le(root.get("salary"), salary);
    }

    public static Specification<JobPosting> belongsToCompany(
        Integer companyId
    ) {
        return (root, query, cb) ->
            cb.equal(root.get("company").get("id"), companyId);
    }
}
