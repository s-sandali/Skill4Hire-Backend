package com.se.skill4hire.entity;

import jakarta.persistence.*;

import java.time.Instant;

public class Notification {
    @Entity
    @Table(
            name = "candidate_preferred_job_role",
            uniqueConstraints = @UniqueConstraint(columnNames = {"candidate_id", "job_role"})
    )
    public static class CandidatePreferredJobRole {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @Column(name = "candidate_id", nullable = false)
        private Long candidateId;

        @Column(name = "job_role", nullable = false)
        private String jobRole;

        @Column(nullable = false, updatable = false)
        private Instant createdAt;

        @PrePersist
        void onCreate() { this.createdAt = Instant.now(); }

        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }

        public Long getCandidateId() { return candidateId; }
        public void setCandidateId(Long candidateId) { this.candidateId = candidateId; }

        public String getJobRole() { return jobRole; }
        public void setJobRole(String jobRole) { this.jobRole = jobRole; }

        public Instant getCreatedAt() { return createdAt; }
    }
}
