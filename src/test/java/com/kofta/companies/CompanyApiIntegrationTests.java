package com.kofta.companies;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kofta.TestSecurityConfig;
import com.kofta.auth.SecurityValidator;
import com.kofta.companies.jobpostings.JobPostingRepository;
import com.kofta.skills.Skill;
import com.kofta.skills.SkillRepository;
import com.kofta.softwareengineers.SoftwareEngineerRepository;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Import(TestSecurityConfig.class)
class CompanyApiIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private JobPostingRepository jobPostingRepository;

    @Autowired
    private SkillRepository skillRepository;

    @Autowired
    private SoftwareEngineerRepository softwareEngineerRepository;

    @MockitoBean
    private SecurityValidator securityValidator;

    @BeforeEach
    void allowCompanyAccess() {
        when(securityValidator.belongsToCompany(anyInt())).thenReturn(true);
        when(securityValidator.isSelfEngineer(anyInt())).thenReturn(true);
    }

    @Test
    void manageCompanyJobPostingLifecycle() throws Exception {
        var companyJson = objectMapper.writeValueAsString(
            Map.of("name", "Acme", "hqLocation", "NYC")
        );

        var companyResult = mockMvc
            .perform(
                post("/companies")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(companyJson)
            )
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").isNumber())
            .andReturn();

        var companyId = objectMapper
            .readTree(companyResult.getResponse().getContentAsString())
            .get("id")
            .asInt();

        var skillId = createSkill("Java");

        var postingJson = objectMapper.writeValueAsString(
            Map.of(
                "title",
                "Backend Engineer",
                "description",
                "Build and maintain backend APIs for clients.",
                "salary",
                120000,
                "skillIds",
                List.of(skillId)
            )
        );

        var postingResult = mockMvc
            .perform(
                post("/companies/{id}/job-postings", companyId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(postingJson)
            )
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.title").value("Backend Engineer"))
            .andReturn();

        var postingId = objectMapper
            .readTree(postingResult.getResponse().getContentAsString())
            .get("id")
            .asInt();

        mockMvc
            .perform(
                get(
                    "/companies/{companyId}/job-postings/{postingId}",
                    companyId,
                    postingId
                )
            )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.title").value("Backend Engineer"));

        var updatedJson = objectMapper.writeValueAsString(
            Map.of(
                "title",
                "Senior Backend Engineer",
                "description",
                "Build APIs and mentor junior engineers across teams.",
                "salary",
                150000
            )
        );

        mockMvc
            .perform(
                patch(
                    "/companies/{companyId}/job-postings/{postingId}",
                    companyId,
                    postingId
                )
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(updatedJson)
            )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.title").value("Senior Backend Engineer"));

        mockMvc
            .perform(
                delete(
                    "/companies/{companyId}/job-postings/{postingId}",
                    companyId,
                    postingId
                )
            )
            .andExpect(status().isOk());

        Assertions.assertThat(
            jobPostingRepository.findById(postingId)
        ).isEmpty();
    }

    @Test
    void returnsNotFoundForMissingCompanyJobPostings() throws Exception {
        mockMvc
            .perform(get("/companies/{id}/job-postings", 999))
            .andExpect(status().isNotFound())
            .andExpect(content().contentType("application/problem+json"));
    }

    @Test
    void returnsNotFoundForMissingJobPosting() throws Exception {
        var companyJson = objectMapper.writeValueAsString(
            Map.of("name", "MissingPostingsCo", "hqLocation", "LA")
        );

        var companyResult = mockMvc
            .perform(
                post("/companies")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(companyJson)
            )
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").isNumber())
            .andReturn();

        var companyId = objectMapper
            .readTree(companyResult.getResponse().getContentAsString())
            .get("id")
            .asInt();

        mockMvc
            .perform(
                get(
                    "/companies/{companyId}/job-postings/{postingId}",
                    companyId,
                    9999
                )
            )
            .andExpect(status().isNotFound())
            .andExpect(content().contentType("application/problem+json"));
    }

    @Test
    void managePostingApplicationsAndStatus() throws Exception {
        var companyJson = objectMapper.writeValueAsString(
            Map.of("name", "ApplyCo", "hqLocation", "Giza")
        );

        var companyResult = mockMvc
            .perform(
                post("/companies")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(companyJson)
            )
            .andExpect(status().isCreated())
            .andReturn();

        var companyId = objectMapper
            .readTree(companyResult.getResponse().getContentAsString())
            .get("id")
            .asInt();

        var skillId = createSkill("Java");
        var postingJson = objectMapper.writeValueAsString(
            Map.of(
                "title",
                "Backend Engineer",
                "description",
                "Build and maintain backend APIs for clients.",
                "salary",
                120000,
                "skillIds",
                List.of(skillId)
            )
        );

        var postingResult = mockMvc
            .perform(
                post("/companies/{id}/job-postings", companyId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(postingJson)
            )
            .andExpect(status().isCreated())
            .andReturn();

        var postingId = objectMapper
            .readTree(postingResult.getResponse().getContentAsString())
            .get("id")
            .asInt();

        var engineerId = registerEngineer(
            "Nora",
            Map.of(
                "name",
                "Nora",
                "yearsOfExperience",
                4,
                "skillIds",
                Set.of(skillId)
            )
        );

        var applicationJson = objectMapper.writeValueAsString(
            Map.of("postingId", postingId)
        );

        var applicationResult = mockMvc
            .perform(
                post("/software-engineers/{id}/applications", engineerId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(applicationJson)
            )
            .andExpect(status().isCreated())
            .andReturn();

        var applicationId = objectMapper
            .readTree(applicationResult.getResponse().getContentAsString())
            .get("id")
            .asInt();

        mockMvc
            .perform(
                get(
                    "/companies/{companyId}/job-postings/{postingId}/applications",
                    companyId,
                    postingId
                )
            )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].id").value(applicationId));

        mockMvc
            .perform(
                get(
                    "/companies/{companyId}/job-postings/{postingId}/applications/{applicationId}",
                    companyId,
                    postingId,
                    applicationId
                )
            )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.posting.id").value(postingId))
            .andExpect(jsonPath("$.applicant.id").value(engineerId));

        var inReviewJson = objectMapper.writeValueAsString(
            Map.of("newStatus", "IN_REVIEW")
        );

        mockMvc
            .perform(
                patch(
                    "/companies/{companyId}/applications/{applicationId}/status",
                    companyId,
                    applicationId
                )
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(inReviewJson)
            )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.status").value("IN_REVIEW"));

        var acceptedJson = objectMapper.writeValueAsString(
            Map.of("newStatus", "ACCEPTED")
        );

        mockMvc
            .perform(
                patch(
                    "/companies/{companyId}/applications/{applicationId}/status",
                    companyId,
                    applicationId
                )
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(acceptedJson)
            )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.status").value("ACCEPTED"));

        var invalidJson = objectMapper.writeValueAsString(
            Map.of("newStatus", "REJECTED")
        );

        mockMvc
            .perform(
                patch(
                    "/companies/{companyId}/applications/{applicationId}/status",
                    companyId,
                    applicationId
                )
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(invalidJson)
            )
            .andExpect(status().isUnprocessableEntity())
            .andExpect(content().contentType("application/problem+json"));
    }

    private Integer createSkill(String name) {
        var skill = new Skill();
        skill.setName(name);
        return skillRepository.save(skill).getId();
    }

    private Integer registerEngineer(
        String name,
        Map<String, Object> profilePayload
    ) throws Exception {
        var registerJson = objectMapper.writeValueAsString(
            Map.of(
                "email",
                name.toLowerCase() + "@example.com",
                "password",
                "secret123",
                "profile",
                profilePayload
            )
        );

        mockMvc
            .perform(
                post("/auth/register/engineer")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(registerJson)
            )
            .andExpect(status().isCreated());

        return softwareEngineerRepository
            .findAll()
            .stream()
            .filter(eng -> name.equals(eng.getName()))
            .findFirst()
            .orElseThrow()
            .getId();
    }
}
