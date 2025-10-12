package com.se.skill4hire.job;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.se.skill4hire.entity.job.JobPost;
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
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
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
class JobPostControllerIntegrationTest {

    private static final int MONGO_PORT = 27028;
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

    @BeforeEach
    void setup() {
        jobPostRepository.deleteAll();
    }

    @Test
    void company_can_create_search_update_and_delete_job() throws Exception {
        // Create job as COMPANY
        JobPost job = new JobPost();
        job.setTitle("Senior Java Engineer");
        job.setDescription("Work with Spring Boot and microservices");
        job.setType("FULL_TIME");
        job.setLocation("Stockholm");
        job.setSalary(80000.0);
        job.setExperience(5);
        job.setDeadline(LocalDate.now().plusDays(30));
        job.setSkills(List.of("java", "spring", "docker"));

        String body = objectMapper.writeValueAsString(job);

        String created = mockMvc.perform(post("/api/jobposts")
                        .sessionAttr("userId", "comp1")
                        .sessionAttr("role", "COMPANY")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.companyId").value("comp1"))
                .andReturn()
                .getResponse()
                .getContentAsString();

        JobPost createdJob = objectMapper.readValue(created, JobPost.class);

        // Public get all
        mockMvc.perform(get("/api/jobposts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Senior Java Engineer"));

        // Search by skill + salary range
        mockMvc.perform(get("/api/jobposts/search")
                        .param("skill", "spring")
                        .param("minSalary", "70000")
                        .param("maxSalary", "90000"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(createdJob.getId()));

        // Update as owner
        createdJob.setSalary(82000.0);
        mockMvc.perform(put("/api/jobposts/" + createdJob.getId())
                        .sessionAttr("userId", "comp1")
                        .sessionAttr("role", "COMPANY")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createdJob)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.salary").value(82000.0));

        // Delete as owner
        mockMvc.perform(delete("/api/jobposts/" + createdJob.getId())
                        .sessionAttr("userId", "comp1")
                        .sessionAttr("role", "COMPANY"))
                .andExpect(status().isNoContent());

        assertThat(jobPostRepository.findById(createdJob.getId())).isEmpty();
    }
}
