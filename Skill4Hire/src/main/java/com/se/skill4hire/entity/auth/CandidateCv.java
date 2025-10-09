package com.se.skill4hire.entity.auth;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "candidate_cv")
public class CandidateCv {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long candidateId;

    @Column(nullable = false)
    private String filename;

    @Column(nullable = false)
    private String contentType;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(nullable = false)
    private byte[] data;

    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @Column(nullable = false)
    private Instant updatedAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CvStatus status;

    public enum CvStatus {
        UPLOADED,
        UNDER_REVIEW,
        APPROVED,
        REJECTED
    }

    @PrePersist
    void onCreate() {
        this.createdAt = this.updatedAt = Instant.now();
        if (this.status == null) {
            this.status = CvStatus.UPLOADED;
        }
    }

    @PreUpdate
    void onUpdate() {
        this.updatedAt = Instant.now();
    }

    // Getters & setters
    public Long getId() { return id; }
    public Long getCandidateId() { return candidateId; }
    public String getFilename() { return filename; }
    public String getContentType() { return contentType; }
    public byte[] getData() { return data; }
    public CvStatus getStatus() { return status; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }

    public void setId(Long id) { this.id = id; }

    public void setCandidateId(Long candidateId) { this.candidateId = candidateId; }

    public void setFilename(String filename) { this.filename = filename; }

    public void setContentType(String contentType) { this.contentType = contentType; }

    public void setData(byte[] data) { this.data = data; }

    public void setStatus(CvStatus status) { this.status = status; }
}
