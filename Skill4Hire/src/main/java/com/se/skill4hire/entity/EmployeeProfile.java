package com.se.skill4hire.entity;

import com.se.skill4hire.entity.auth.Employee;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "employee_profiles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private Employee user;

    @Column(nullable = false, length = 255)
    private String name;

    @Column(nullable = false, unique = true, length = 255)
    private String email;

    @Size(max = 15)
    @Column(name = "phone_number", length = 15)
    private String phoneNumber;

    @Size(max = 100)
    @Column(length = 100)
    private String location;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Size(max = 100)
    @Column(length = 100)
    private String title;

    @Column(length = 500)
    private String headline;

    @ElementCollection
    @CollectionTable(
            name = "employee_skills",
            joinColumns = @JoinColumn(name = "employee_id")
    )
    @Column(name = "skill")
    private List<String> skills;

    @Column(name = "education", length = 1000)
    private String education;

    @Column(name = "experience", length = 2000)
    private String experience;

    @Column(name = "notification_preferences", length = 500)
    private String notificationPreferences;

    @Column(name = "profile_picture_path", length = 255)
    private String profilePicturePath;

    @Column(name = "profile_completeness")
    private Double profileCompleteness;

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