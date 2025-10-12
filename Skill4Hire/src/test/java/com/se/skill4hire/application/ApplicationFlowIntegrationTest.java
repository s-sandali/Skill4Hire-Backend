package com.se.skill4hire.application;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.se.skill4hire.entity.job.JobPost;
import com.se.skill4hire.repository.ApplicationRepository;
import com.se.skill4hire.repository.job.JobPostRepository;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(properties = "spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration")
@AutoConfigureMockMvc
class ApplicationFlowIntegrationTest {

    private static final int MONGO_PORT = 27029;
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
    JobPostRepository jobPostRepository;

    @Autowired
    ApplicationRepository applicationRepository;

    @BeforeEach
    void setup() {
        applicationRepository.deleteAll();
        jobPostRepository.deleteAll();
    }

    @Test
    void candidate_can_apply_and_track_status() throws Exception {
        // Create job as COMPANY
        JobPost job = new JobPost();
        job.setTitle("Backend Engineer");
        job.setDescription("Build APIs with Spring Boot");
        job.setType("FULL_TIME");
        job.setLocation("Gothenburg");
        job.setSalary(60000.0);
        job.setExperience(3);
        job.setDeadline(LocalDate.now().plusDays(15));
        job.setSkills(List.of("java", "spring"));

        String createdJobJson = mockMvc.perform(post("/api/jobposts")
                        .sessionAttr("userId", "comp2")
                        .sessionAttr("role", "COMPANY")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(job)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andReturn().getResponse().getContentAsString();
        JsonNode createdJob = objectMapper.readTree(createdJobJson);
        String jobId = createdJob.get("id").asText();

        // Candidate applies
        String applyPayload = "{\"jobPostId\":\"" + jobId + "\"}";
        String applicationJson = mockMvc.perform(post("/api/candidates/applications")
                        .sessionAttr("userId", "cand1")
                        .sessionAttr("role", "CANDIDATE")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(applyPayload))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value("APPLIED"))
                .andReturn().getResponse().getContentAsString();
        JsonNode applicationDto = objectMapper.readTree(applicationJson);
        String applicationId = applicationDto.get("id").asText();

        // Candidate lists applications
        mockMvc.perform(get("/api/candidates/applications")
                        .sessionAttr("userId", "cand1")
                        .sessionAttr("role", "CANDIDATE"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].jobPostId").value(jobId))
                .andExpect(jsonPath("$[0].status").value("APPLIED"));

        // Employee updates status to INTERVIEW
        String statusUpdate = "{\"status\":\"INTERVIEW\"}";
        mockMvc.perform(put("/api/employees/applications/" + applicationId + "/status")
                        .sessionAttr("userId", "emp1")
                        .sessionAttr("role", "EMPLOYEE")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(statusUpdate))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("INTERVIEW"));

        assertThat(applicationRepository.findAll()).hasSize(1);
        assertThat(applicationRepository.findAll().get(0).getStatus().name()).isEqualTo("INTERVIEW");
    }
}
