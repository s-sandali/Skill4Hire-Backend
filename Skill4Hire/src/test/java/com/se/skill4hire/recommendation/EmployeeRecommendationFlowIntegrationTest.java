package com.se.skill4hire.recommendation;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.se.skill4hire.entity.profile.CandidateProfile;
import com.se.skill4hire.repository.RecommendationRepository;
import com.se.skill4hire.repository.job.JobPostRepository;
import com.se.skill4hire.repository.profile.CandidateProfileRepository;
import de.flapdoodle.embed.mongo.MongodExecutable;
import de.flapdoodle.embed.mongo.MongodProcess;
import de.flapdoodle.embed.mongo.MongodStarter;
import de.flapdoodle.embed.mongo.config.MongodConfig;
import de.flapdoodle.embed.mongo.config.Net;
import de.flapdoodle.embed.mongo.distribution.Version;
import de.flapdoodle.embed.process.runtime.Network;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(properties = "spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration")
@AutoConfigureMockMvc
class EmployeeRecommendationFlowIntegrationTest {

    private static final int MONGO_PORT = 27027; // distinct from other tests
    private static MongodExecutable mongodExe;
    private static MongodProcess mongod;
    private static final String MONGO_URI = "mongodb://localhost:" + MONGO_PORT + "/skill4hire-it";

    static {
        try {
            MongodConfig config = MongodConfig.builder()
                    .version(Version.Main.PRODUCTION)
                    .net(new Net("localhost", MONGO_PORT, Network.localhostIsIPv6()))
                    .build();
            MongodStarter starter = MongodStarter.getDefaultInstance();
            mongodExe = starter.prepare(config);
            mongod = mongodExe.start();
        } catch (IOException e) {
            throw new RuntimeException("Failed to start embedded MongoDB", e);
        }
    }

    @DynamicPropertySource
    static void mongoProps(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", () -> MONGO_URI);
        registry.add("spring.data.mongodb.database", () -> "skill4hire-it");
    }

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    CandidateProfileRepository candidateProfileRepository;

    @Autowired
    JobPostRepository jobPostRepository;

    @Autowired
    RecommendationRepository recommendationRepository;

    @BeforeEach
    void setup() {
        recommendationRepository.deleteAll();
        jobPostRepository.deleteAll();
        candidateProfileRepository.deleteAll();
    }

    @Test
    void employee_can_recommend_candidate_and_company_can_view_recommendations() throws Exception {
        // Seed a candidate profile so recommendation passes validation
        CandidateProfile profile = new CandidateProfile();
        profile.setUserId("candR1");
        profile.setName("Candidate R1");
        profile.setEmail("candR1@example.com");
        profile.setSkills(List.of("java", "spring"));
        candidateProfileRepository.save(profile);

        // Company creates a job post
        String jobJson = "{" +
                "\"title\":\"Java Developer\"," +
                "\"description\":\"APIs with Spring\"," +
                "\"type\":\"FULL_TIME\"," +
                "\"location\":\"Stockholm\"," +
                "\"salary\":70000," +
                "\"experience\":3," +
                "\"skills\":[\"java\",\"spring\"]," +
                "\"deadline\":\"" + LocalDate.now().plusDays(20) + "\"" +
                "}";

        String createdJobJson = mockMvc.perform(post("/api/jobposts")
                        .sessionAttr("userId", "compR1")
                        .sessionAttr("role", "COMPANY")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jobJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.companyId").value("compR1"))
                .andReturn().getResponse().getContentAsString();
        JsonNode createdJob = objectMapper.readTree(createdJobJson);
        String jobId = createdJob.get("id").asText();

        // Employee recommends candidate to the job
        String recommendPayload = "{" +
                "\"candidateId\":\"candR1\"," +
                "\"jobId\":\"" + jobId + "\"," +
                "\"note\":\"Strong fit for the role\"" +
                "}";

        mockMvc.perform(post("/api/employees/recommendations")
                        .sessionAttr("userId", "empR1")
                        .sessionAttr("role", "EMPLOYEE")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(recommendPayload))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.employeeId").value("empR1"))
                .andExpect(jsonPath("$.candidateId").value("candR1"))
                .andExpect(jsonPath("$.jobId").value(jobId));

        // Employee lists own recommendations (paged)
        mockMvc.perform(get("/api/employees/recommendations")
                        .param("page", "0")
                        .param("size", "10")
                        .sessionAttr("userId", "empR1")
                        .sessionAttr("role", "EMPLOYEE"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].candidateId").value("candR1"))
                .andExpect(jsonPath("$.content[0].jobId").value(jobId));

        // Company lists all recommendations across its jobs (paged)
        mockMvc.perform(get("/api/companies/recommendations")
                        .param("page", "0")
                        .param("size", "10")
                        .sessionAttr("userId", "compR1")
                        .sessionAttr("role", "COMPANY"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].candidateId").value("candR1"))
                .andExpect(jsonPath("$.content[0].jobId").value(jobId));

        // Company lists recommendations for the specific job (ownership enforced)
        mockMvc.perform(get("/api/companies/jobs/" + jobId + "/recommendations")
                        .param("page", "0")
                        .param("size", "10")
                        .sessionAttr("userId", "compR1")
                        .sessionAttr("role", "COMPANY"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].candidateId").value("candR1"))
                .andExpect(jsonPath("$.content[0].jobId").value(jobId));
    }
}

