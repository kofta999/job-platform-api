package com.kofta.softwareengineers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kofta.companies.Company;
import com.kofta.companies.CompanyRepository;
import com.kofta.companies.jobpostings.JobPosting;
import com.kofta.companies.jobpostings.JobPostingRepository;
import com.kofta.skills.Skill;
import com.kofta.skills.SkillRepository;
import java.util.Map;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class SoftwareEngineerApiIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private SkillRepository skillRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private JobPostingRepository jobPostingRepository;

    @Test
    void createUpdateProfileAndDeleteEngineer() throws Exception {
        var skillId = createSkill("Java");

        var engineerId = createEngineer(
            Map.of(
                "name",
                "Mona",
                "yearsOfExperience",
                4,
                "skillIds",
                Set.of(skillId)
            )
        );

        mockMvc
            .perform(get("/software-engineers/{id}", engineerId))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name").value("Mona"))
            .andExpect(jsonPath("$.skills[0].id").value(skillId));

        var profileJson = objectMapper.writeValueAsString(
            Map.of(
                "bio",
                "Backend engineer",
                "githubUrl",
                "https://github.com/mona",
                "linkedinUrl",
                "https://www.linkedin.com/in/mona",
                "location",
                "Cairo"
            )
        );

        mockMvc
            .perform(
                post("/software-engineers/{id}/profile", engineerId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(profileJson)
            )
            .andExpect(status().isCreated());

        mockMvc
            .perform(get("/software-engineers/{id}", engineerId))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.profile.bio").value("Backend engineer"))
            .andExpect(jsonPath("$.profile.location").value("Cairo"));

        var updateEngineerJson = objectMapper.writeValueAsString(
            Map.of("name", "Mona A.", "yearsOfExperience", 5)
        );

        mockMvc
            .perform(
                patch("/software-engineers/{id}", engineerId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(updateEngineerJson)
            )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name").value("Mona A."));

        var updateProfileJson = objectMapper.writeValueAsString(
            Map.of("location", "Alexandria")
        );

        mockMvc
            .perform(
                patch("/software-engineers/{id}/profile", engineerId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(updateProfileJson)
            )
            .andExpect(status().isOk());

        mockMvc
            .perform(get("/software-engineers/{id}", engineerId))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.profile.location").value("Alexandria"));

        mockMvc
            .perform(delete("/software-engineers/{id}", engineerId))
            .andExpect(status().isNoContent());

        mockMvc
            .perform(get("/software-engineers/{id}", engineerId))
            .andExpect(status().isNotFound());
    }

    @Test
    void submitAndFetchApplications() throws Exception {
        var skillId = createSkill("Go");
        var engineerId = createEngineer(
            Map.of(
                "name",
                "Hassan",
                "yearsOfExperience",
                3,
                "skillIds",
                Set.of(skillId)
            )
        );

        var postingId = createJobPosting("Backend Engineer", 120000);

        var applicationJson = objectMapper.writeValueAsString(
            Map.of("postingId", postingId)
        );

        var submitResult = mockMvc
            .perform(
                post("/software-engineers/{id}/applications", engineerId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(applicationJson)
            )
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").isNumber())
            .andReturn();

        var applicationId = objectMapper
            .readTree(submitResult.getResponse().getContentAsString())
            .get("id")
            .asInt();

        mockMvc
            .perform(get("/software-engineers/{id}/applications", engineerId))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].id").value(applicationId));

        mockMvc
            .perform(
                get(
                    "/software-engineers/{id}/applications/{appId}",
                    engineerId,
                    applicationId
                )
            )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.posting.id").value(postingId))
            .andExpect(jsonPath("$.applicant.id").value(engineerId));
    }

    @Test
    void returnsNotFoundForMissingEngineerAndApplication() throws Exception {
        mockMvc
            .perform(get("/software-engineers/{id}", 999))
            .andExpect(status().isNotFound());

        var postingId = createJobPosting("Frontend Engineer", 90000);

        var applicationJson = objectMapper.writeValueAsString(
            Map.of("postingId", postingId)
        );

        mockMvc
            .perform(
                post("/software-engineers/{id}/applications", 999)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(applicationJson)
            )
            .andExpect(status().isNotFound());

        var skillId = createSkill("Python");
        var engineerId = createEngineer(
            Map.of(
                "name",
                "Laila",
                "yearsOfExperience",
                2,
                "skillIds",
                Set.of(skillId)
            )
        );

        mockMvc
            .perform(
                get(
                    "/software-engineers/{id}/applications/{appId}",
                    engineerId,
                    9999
                )
            )
            .andExpect(status().isNotFound());
    }

    private Integer createSkill(String name) {
        var skill = new Skill();
        skill.setName(name);
        return skillRepository.save(skill).getId();
    }

    private Integer createEngineer(Map<String, Object> payload)
        throws Exception {
        var engineerJson = objectMapper.writeValueAsString(payload);

        var result = mockMvc
            .perform(
                post("/software-engineers")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(engineerJson)
            )
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").isNumber())
            .andReturn();

        return objectMapper
            .readTree(result.getResponse().getContentAsString())
            .get("id")
            .asInt();
    }

    private Integer createJobPosting(String title, Integer salary) {
        var company = new Company();
        company.setName("TestCo");
        company.setHqLocation("Giza");
        var savedCompany = companyRepository.save(company);

        var posting = new JobPosting();
        posting.setTitle(title);
        posting.setDescription("Build services");
        posting.setSalary(salary);
        posting.setCompany(savedCompany);

        return jobPostingRepository.save(posting).getId();
    }
}
