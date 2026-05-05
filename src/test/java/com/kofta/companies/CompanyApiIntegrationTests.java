package com.kofta.companies;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kofta.companies.jobpostings.JobPostingRepository;
import java.util.Map;
import org.assertj.core.api.Assertions;
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
class CompanyApiIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private JobPostingRepository jobPostingRepository;

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

        var postingJson = objectMapper.writeValueAsString(
            Map.of(
                "title",
                "Backend Engineer",
                "description",
                "Build APIs",
                "salary",
                120000
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
                "Build APIs and mentor juniors",
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
            .andExpect(status().isNotFound());
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
            .andExpect(status().isNotFound());
    }
}
