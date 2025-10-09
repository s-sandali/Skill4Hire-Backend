package com.se.skill4hire.entity.candidate;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.Instant;

@Document(collection = "candidate_cv")
public class CandidateCv {
    @Id
    private String id;

    private String candidateId;

    private String filename;

    private String contentType;

    private byte[] data;

    private Instant createdAt;

    private Instant updatedAt;

    private CvStatus status;

    public enum CvStatus {
        UPLOADED,
        UNDER_REVIEW,
        APPROVED,
        REJECTED
    }

    public CandidateCv() {
        this.createdAt = this.updatedAt = Instant.now();
        if (this.status == null) {
            this.status = CvStatus.UPLOADED;
        }
    }

    // Getters & setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getCandidateId() { return candidateId; }
    public void setCandidateId(String candidateId) { this.candidateId = candidateId; }
    public String getFilename() { return filename; }
    public void setFilename(String filename) { this.filename = filename; }
    public String getContentType() { return contentType; }
    public void setContentType(String contentType) { this.contentType = contentType; }
    public byte[] getData() { return data; }
    public void setData(byte[] data) { this.data = data; }
    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }
    public CvStatus getStatus() { return status; }
    public void setStatus(CvStatus status) { this.status = status; }
}
