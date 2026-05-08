package com.kofta;

import com.kofta.auth.Role;
import com.kofta.auth.User;
import com.kofta.auth.UserRepository;
import com.kofta.companies.Company;
import com.kofta.companies.CompanyRepository;
import com.kofta.companies.jobpostings.JobPosting;
import com.kofta.companies.jobpostings.JobPostingRepository;
import com.kofta.engineerprofiles.EngineerProfile;
import com.kofta.engineerprofiles.EngineerProfileRepository;
import com.kofta.skills.Skill;
import com.kofta.skills.SkillRepository;
import com.kofta.softwareengineers.SoftwareEngineer;
import com.kofta.softwareengineers.SoftwareEngineerRepository;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Profile("dev")
public class DataInitializer implements CommandLineRunner {

    private final SkillRepository skillRepository;
    private final SoftwareEngineerRepository softwareEngineerRepository;
    private final EngineerProfileRepository engineerProfileRepository;
    private final CompanyRepository companyRepository;
    private final JobPostingRepository jobPostingRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(String... args) {
        if (
            skillRepository.count() > 0 ||
            softwareEngineerRepository.count() > 0 ||
            companyRepository.count() > 0 ||
            jobPostingRepository.count() > 0 ||
            userRepository.count() > 0
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

        var aliceUser = new User();
        aliceUser.setEmail("alice@demo.com");
        aliceUser.setPassword(passwordEncoder.encode("alice123"));
        aliceUser.setRole(Role.ROLE_ENGINEER);
        aliceUser.setEnabled(true);
        aliceUser.setSoftwareEngineer(alice);
        aliceUser = userRepository.save(aliceUser);

        alice.setUser(aliceUser);

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

        var bobUser = new User();
        bobUser.setEmail("bob@demo.com");
        bobUser.setPassword(passwordEncoder.encode("bob123"));
        bobUser.setRole(Role.ROLE_ENGINEER);
        bobUser.setEnabled(true);
        bobUser.setSoftwareEngineer(bob);
        bobUser = userRepository.save(bobUser);

        bob.setUser(bobUser);

        softwareEngineerRepository.saveAll(List.of(alice, bob));

        var acme = new Company();
        acme.setName("Acme Corp");
        acme.setHqLocation("NYC");
        acme = companyRepository.save(acme);

        var acmeUser = new User();
        acmeUser.setEmail("hr@acme.com");
        acmeUser.setPassword(passwordEncoder.encode("acme123"));
        acmeUser.setRole(Role.ROLE_COMPANY);
        acmeUser.setCompany(acme);
        acmeUser.setEnabled(true);
        userRepository.save(acmeUser);

        var backendPosting = new JobPosting();
        backendPosting.setTitle("Backend Engineer");
        backendPosting.setDescription("Build and maintain REST APIs.");
        backendPosting.setSalary(120000);
        backendPosting.setCompany(acme);
        backendPosting.setSkills(Set.of(java, spring, postgres));

        var platformPosting = new JobPosting();
        platformPosting.setTitle("Platform Engineer");
        platformPosting.setDescription(
            "Own infrastructure and deployment pipelines."
        );
        platformPosting.setSalary(140000);
        platformPosting.setCompany(acme);
        platformPosting.setSkills(Set.of(postgres));

        jobPostingRepository.saveAll(List.of(backendPosting, platformPosting));
    }
}
