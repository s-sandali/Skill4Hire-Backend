package com.se.skill4hire.company;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.se.skill4hire.dto.application.ApplicationStatusUpdateRequest;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(properties = "spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration")
@AutoConfigureMockMvc
class CompanyApplicantFlowIntegrationTest {

    private static final int MONGO_PORT = 27026; // dedicated port
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
    void company_can_move_applicant_through_stages_and_candidate_views_reflect() throws Exception {
        // Company creates a job post
        String jobJson = "{" +
                "\"title\":\"Platform Engineer\"," +
                "\"description\":\"Infra and CI/CD\"," +
                "\"type\":\"FULL_TIME\"," +
                "\"location\":\"Gothenburg\"," +
                "\"salary\":90000," +
                "\"experience\":4," +
                "\"skills\":[\"aws\",\"kubernetes\"]," +
                "\"deadline\":\"" + LocalDate.now().plusDays(25) + "\"" +
                "}";

        String createdJobJson = mockMvc.perform(post("/api/jobposts")
                        .sessionAttr("userId", "compC1")
                        .sessionAttr("role", "COMPANY")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jobJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.companyId").value("compC1"))
                .andReturn().getResponse().getContentAsString();
        JsonNode createdJob = objectMapper.readTree(createdJobJson);
        String jobId = createdJob.get("id").asText();

        // Candidate applies to the job
        String applyPayload = "{\"jobPostId\":\"" + jobId + "\"}";
        String applicationJson = mockMvc.perform(post("/api/candidates/applications")
                        .sessionAttr("userId", "candC1")
                        .sessionAttr("role", "CANDIDATE")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(applyPayload))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value("APPLIED"))
                .andReturn().getResponse().getContentAsString();
        JsonNode applicationDto = objectMapper.readTree(applicationJson);
        String applicationId = applicationDto.get("id").asText();

        // Company lists their applications for the job
        mockMvc.perform(get("/api/companies/jobs/" + jobId + "/applications")
                        .sessionAttr("userId", "compC1")
                        .sessionAttr("role", "COMPANY"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(applicationId));

        // 1) SHORTLIST
        mockMvc.perform(put("/api/companies/applications/" + applicationId + "/status")
                        .sessionAttr("userId", "compC1")
                        .sessionAttr("role", "COMPANY")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"status\":\"SHORTLISTED\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("SHORTLISTED"));

        // Candidate can see updated status on list (session-scoped endpoint)
        mockMvc.perform(get("/api/candidates/applications")
                        .sessionAttr("userId", "candC1")
                        .sessionAttr("role", "CANDIDATE"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].status").value("SHORTLISTED"));

        // 2) INTERVIEW
        mockMvc.perform(put("/api/companies/applications/" + applicationId + "/status")
                        .sessionAttr("userId", "compC1")
                        .sessionAttr("role", "COMPANY")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"status\":\"INTERVIEW\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("INTERVIEW"));

        // Candidate sees INTERVIEW status
        mockMvc.perform(get("/api/candidates/applications")
                        .sessionAttr("userId", "candC1")
                        .sessionAttr("role", "CANDIDATE"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].status").value("INTERVIEW"));

        // 3) HIRED
        mockMvc.perform(put("/api/companies/applications/" + applicationId + "/status")
                        .sessionAttr("userId", "compC1")
                        .sessionAttr("role", "COMPANY")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"status\":\"HIRED\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("HIRED"));

        // Candidate sees HIRED status
        mockMvc.perform(get("/api/candidates/applications")
                        .sessionAttr("userId", "candC1")
                        .sessionAttr("role", "CANDIDATE"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].status").value("HIRED"));

        // 4) REJECTED without reason -> 400
        mockMvc.perform(put("/api/companies/applications/" + applicationId + "/status")
                        .sessionAttr("userId", "compC1")
                        .sessionAttr("role", "COMPANY")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"status\":\"REJECTED\"}"))
                .andExpect(status().isBadRequest());

        // 4b) REJECTED with reason -> 200 (use DTO to ensure valid JSON)
        ApplicationStatusUpdateRequest rejectReq = new ApplicationStatusUpdateRequest();
        rejectReq.setStatus("REJECTED");
        rejectReq.setReason("Failed final round");
        String rejectJson = objectMapper.writeValueAsString(rejectReq);

        mockMvc.perform(put("/api/companies/applications/" + applicationId + "/status")
                        .sessionAttr("userId", "compC1")
                        .sessionAttr("role", "COMPANY")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(rejectJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("REJECTED"));

        // Candidate summary reflects counts (session-scoped summary endpoint)
        mockMvc.perform(get("/api/candidates/applications/summary")
                        .sessionAttr("userId", "candC1")
                        .sessionAttr("role", "CANDIDATE"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.applied").value(0))
                .andExpect(jsonPath("$.rejected").value(1));
    }
}