package com.kofta;

import com.kofta.engineerprofiles.EngineerProfile;
import com.kofta.engineerprofiles.EngineerProfileRepository;
import com.kofta.skills.Skill;
import com.kofta.skills.SkillRepository;
import com.kofta.softwareengineers.SoftwareEngineer;
import com.kofta.softwareengineers.SoftwareEngineerRepository;
import java.util.List;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class DataInitializer implements CommandLineRunner {

    private final SkillRepository skillRepository;
    private final SoftwareEngineerRepository softwareEngineerRepository;
    private final EngineerProfileRepository engineerProfileRepository;

    public DataInitializer(
        SkillRepository skillRepository,
        SoftwareEngineerRepository softwareEngineerRepository,
        EngineerProfileRepository engineerProfileRepository
    ) {
        this.skillRepository = skillRepository;
        this.softwareEngineerRepository = softwareEngineerRepository;
        this.engineerProfileRepository = engineerProfileRepository;
    }

    @Override
    @Transactional
    public void run(String... args) {
        if (
            skillRepository.count() > 0 ||
            softwareEngineerRepository.count() > 0
        ) {
            return;
        }

        var java = new Skill();
        java.setName("Java");

        var spring = new Skill();
        spring.setName("Spring Boot");

        var postgres = new Skill();
        postgres.setName("PostgreSQL");

        skillRepository.saveAll(List.of(java, spring, postgres));

        var alice = new SoftwareEngineer(null, "Alice", 5);
        alice.addSkill(java);
        alice.addSkill(spring);

        var aliceProfile = new EngineerProfile();
        aliceProfile.setBio("Backend engineer focused on Spring Boot APIs.");
        aliceProfile.setGithubUrl("https://github.com/alice");
        aliceProfile.setLinkedinUrl("https://www.linkedin.com/in/alice");
        aliceProfile.setLocation("Cairo, Egypt");
        engineerProfileRepository.save(aliceProfile);
        alice.setProfile(aliceProfile);

        var bob = new SoftwareEngineer(null, "Bob", 2);
        bob.addSkill(java);
        bob.addSkill(postgres);

        softwareEngineerRepository.saveAll(List.of(alice, bob));
    }
}
