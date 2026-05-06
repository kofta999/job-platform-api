package com.kofta.softwareengineers;

import com.kofta.auth.User;
import com.kofta.engineerprofiles.EngineerProfile;
import com.kofta.jobapplications.JobApplication;
import com.kofta.skills.Skill;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
public class SoftwareEngineer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    @ManyToMany(
        cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE,
            CascadeType.REFRESH,
            CascadeType.DETACH,
        }
    )
    @JoinTable(
        name = "engineer_skills",
        joinColumns = @JoinColumn(name = "engineer_id"),
        inverseJoinColumns = @JoinColumn(name = "skill_id")
    )
    private Set<Skill> skills = new HashSet<>();

    @Column(name = "years_of_experience")
    private Integer yearsOfExperience;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "profile_id", referencedColumnName = "id")
    private EngineerProfile profile;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "applicant")
    private Set<JobApplication> applications = new HashSet<>();

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", unique = true, nullable = false)
    private User user;

    public SoftwareEngineer() {}

    public SoftwareEngineer(
        Integer id,
        String name,
        Integer yearsOfExperience
    ) {
        this.id = id;
        this.name = name;
        this.yearsOfExperience = yearsOfExperience;
    }

    public void addSkill(Skill skill) {
        this.skills.add(skill);
    }

    @Override
    public boolean equals(Object o) {
        if (o != null && getClass() != o.getClass()) return false;
        var that = (SoftwareEngineer) o;
        return (Objects.equals(id, that.id) && Objects.equals(name, that.name));
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
