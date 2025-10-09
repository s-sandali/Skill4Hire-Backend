package com.se.skill4hire.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

public class Notification {
    @Document(collection = "candidate_preferred_job_role")
    public static class CandidatePreferredJobRole {
        @Id
        private String id;
        private String candidateId;
        private String jobRole;
        private Instant createdAt;

        public CandidatePreferredJobRole() {
            this.createdAt = Instant.now();
        }

        public String getId() { return id; }
        public void setId(String id) { this.id = id; }

        public String getCandidateId() { return candidateId; }
        public void setCandidateId(String candidateId) { this.candidateId = candidateId; }

        public String getJobRole() { return jobRole; }
        public void setJobRole(String jobRole) { this.jobRole = jobRole; }

        public Instant getCreatedAt() { return createdAt; }
    }
}
