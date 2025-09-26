package com.se.skill4hire.entity.profile;

import com.se.skill4hire.entity.auth.Candidate;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "candidate_profiles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CandidateProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private Candidate user;

    @Size(max = 15, message = "Phone number must be less than 15 characters")
    private String phoneNumber;

    @Size(max = 100, message = "Location must be less than 100 characters")
    private String location;

    private LocalDate dateOfBirth;

    @Size(max = 100, message = "Title must be less than 100 characters")
    private String title;

    @Column(length = 500)
    private String headline;

    @ElementCollection
    @CollectionTable(name = "candidate_skills", joinColumns = @JoinColumn(name = "candidate_profile_id"))
    @Column(name = "skill")
    private List<String> skills = new ArrayList<>();

    @Embedded
    private Education education;

    @Embedded
    private Experience experience;

    @Embedded
    private JobPreferences jobPreferences;

    @Embedded
    private NotificationPreferences notificationPreferences;

    private String resumePath;
    private String profilePicturePath;
    private Double profileCompleteness = 0.0;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}