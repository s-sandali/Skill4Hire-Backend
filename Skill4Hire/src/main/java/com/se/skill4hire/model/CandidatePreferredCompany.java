package com.se.skill4hire.model;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(
        name = "candidate_preferred_company",
        uniqueConstraints = @UniqueConstraint(columnNames = {"candidate_id", "company_name"})
)
public class CandidatePreferredCompany {
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

