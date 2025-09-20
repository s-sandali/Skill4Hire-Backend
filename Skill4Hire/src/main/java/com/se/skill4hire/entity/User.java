package com.se.skill4hire.entity;

import jakarta.persistence.*;

import java.time.Instant;

@MappedSuperclass
public abstract class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;
    private String password;

    @Column(nullable = false)
    private String role; // CANDIDATE, COMPANY, EMPLOYEE, ADMIN

    public User() {}

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    // Getters and setters
    public Long getId() { return id; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getRole() { return role; }
    public void setRole(String role) {
        // Validate role
        if (isValidRole(role)) {
            this.role = role.toUpperCase();
        } else {
            throw new IllegalArgumentException("Invalid role: " + role);
        }
    }

    private boolean isValidRole(String role) {
        return role != null &&
                (role.equalsIgnoreCase("CANDIDATE") ||
                        role.equalsIgnoreCase("COMPANY") ||
                        role.equalsIgnoreCase("EMPLOYEE") ||
                        role.equalsIgnoreCase("ADMIN"));
    }

    @Entity
    @Table(name = "candidate_cv")
    public static class CandidateCv {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        /** Link to the candidate/user in your system (could be a FK to a Candidate table). */
        @Column(nullable = false)
        private Long candidateId;

        @Column(nullable = false)
        private String filename;

        @Column(nullable = false)
        private String contentType; // e.g. application/pdf

        @Lob
        @Basic(fetch = FetchType.LAZY)
        @Column(nullable = false)
        private byte[] data;

        @Column(nullable = false, updatable = false)
        private Instant createdAt;

        @Column(nullable = false)
        private Instant updatedAt;

        @PrePersist
        void onCreate() {
            this.createdAt = this.updatedAt = Instant.now();
        }

        @PreUpdate
        void onUpdate() {
            this.updatedAt = Instant.now();
        }

        // Getters & setters
        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }

        public Long getCandidateId() { return candidateId; }
        public void setCandidateId(Long candidateId) { this.candidateId = candidateId; }

        public String getFilename() { return filename; }
        public void setFilename(String filename) { this.filename = filename; }

        public String getContentType() { return contentType; }
        public void setContentType(String contentType) { this.contentType = contentType; }

        public byte[] getData() { return data; }
        public void setData(byte[] data) { this.data = data; }

        public Instant getCreatedAt() { return createdAt; }
        public Instant getUpdatedAt() { return updatedAt; }
    }

    @Entity
    @Table(
            name = "candidate_preferred_company",
            uniqueConstraints = @UniqueConstraint(columnNames = {"candidate_id", "company_name"})
    )
    public static class CandidatePreferredCompany {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @Column(name = "candidate_id", nullable = false)
        private Long candidateId;

        @Column(name = "company_name", nullable = false)
        private String companyName;

        @Column(nullable = false, updatable = false)
        private Instant createdAt;

        @PrePersist
        void onCreate() { this.createdAt = Instant.now(); }

        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }

        public Long getCandidateId() { return candidateId; }
        public void setCandidateId(Long candidateId) { this.candidateId = candidateId; }

        public String getCompanyName() { return companyName; }
        public void setCompanyName(String companyName) { this.companyName = companyName; }

        public Instant getCreatedAt() { return createdAt; }
    }
}