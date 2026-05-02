package com.kofta.softwareEngineers;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.util.Objects;

@Entity
public class SoftwareEngineer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;
    private String techStack;
    private Integer yearsOfExperience;

    public Integer getYearsOfExperience() {
        return yearsOfExperience;
    }

    public void setYearsOfExperience(Integer yearsOfExperience) {
        this.yearsOfExperience = yearsOfExperience;
    }

    public SoftwareEngineer() {}

    public SoftwareEngineer(
        Integer id,
        String name,
        String techStack,
        Integer yearsOfExperience
    ) {
        this.id = id;
        this.name = name;
        this.techStack = techStack;
        this.yearsOfExperience = yearsOfExperience;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTechStack() {
        return techStack;
    }

    public void setTechStack(String techStack) {
        this.techStack = techStack;
    }

    @Override
    public boolean equals(Object o) {
        if (o != null && getClass() != o.getClass()) return false;
        var that = (SoftwareEngineer) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(techStack, that.techStack)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, techStack);
    }
}
