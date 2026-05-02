package com.kofta.softwareEngineers;

import org.springframework.data.jpa.domain.Specification;

public class SoftwareEngineerSpecification {

    public static Specification<SoftwareEngineer> hasTechStack(
        String techStack
    ) {
        return (root, query, cb) ->
            cb.like(root.get("techStack"), "%" + techStack + "%");
    }

    public static Specification<SoftwareEngineer> hasYearsGreaterThan(
        Integer years
    ) {
        return (root, query, cb) -> cb.ge(root.get("yearsOfExperience"), years);
    }
}
