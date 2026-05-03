package com.kofta.skills;

import com.kofta.softwareEngineers.SoftwareEngineer;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Skill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    // Field name in SoftwareEngineer
    @ManyToMany(mappedBy = "skills")
    private Set<SoftwareEngineer> engineers = new HashSet<>();

    public Set<SoftwareEngineer> getEngineers() {
        return engineers;
    }

    public void setEngineers(Set<SoftwareEngineer> engineers) {
        this.engineers = engineers;
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
}
