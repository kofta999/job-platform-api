package com.kofta.softwareEngineers;

import com.kofta.skills.Skill;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;

public class SoftwareEngineerSpecification {

    public static Specification<SoftwareEngineer> hasSkill(String skill) {
        return (root, query, cb) -> {
            Join<SoftwareEngineer, Skill> join = root.join("skills");
            query.distinct(true);
            return cb.equal(cb.lower(join.get("name")), skill.toLowerCase());
        };
    }

    public static Specification<SoftwareEngineer> hasYearsGreaterThanOrEqual(
        Integer years
    ) {
        return (root, query, cb) -> cb.ge(root.get("yearsOfExperience"), years);
    }
}
