package com.kofta.auth;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kofta.TestSecurityConfig;
import com.kofta.skills.Skill;
import com.kofta.skills.SkillRepository;
import java.util.Map;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Import(TestSecurityConfig.class)
class AuthApiIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private SkillRepository skillRepository;

    @Test
    void registerEngineerAndLogin() throws Exception {
        var skill = new Skill();
        skill.setName("Java");
        var skillId = skillRepository.save(skill).getId();

        var registerJson = objectMapper.writeValueAsString(
            Map.of(
                "email",
                "engineer@example.com",
                "password",
                "secret123",
                "profile",
                Map.of(
                    "name",
                    "Dana",
                    "yearsOfExperience",
                    3,
                    "skillIds",
                    Set.of(skillId)
                )
            )
        );

        mockMvc
            .perform(
                post("/auth/register/engineer")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(registerJson)
            )
            .andExpect(status().isCreated());

        var loginJson = objectMapper.writeValueAsString(
            Map.of("email", "engineer@example.com", "password", "secret123")
        );

        mockMvc
            .perform(
                post("/auth/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(loginJson)
            )
            .andExpect(status().isOk());
    }

    @Test
    void registerCompany() throws Exception {
        var registerJson = objectMapper.writeValueAsString(
            Map.of(
                "email",
                "company@example.com",
                "password",
                "secret123",
                "companyDetails",
                Map.of("name", "TestCo", "hqLocation", "Cairo")
            )
        );

        mockMvc
            .perform(
                post("/auth/register/company")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(registerJson)
            )
            .andExpect(status().isCreated());
    }
}
