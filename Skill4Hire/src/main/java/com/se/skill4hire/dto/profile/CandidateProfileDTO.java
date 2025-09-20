package com.se.skill4hire.dto.profile
        ;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CandidateProfileDTO {
    private Long id;

    @NotBlank(message = "Name is required")
    @Size(max = 100, message = "Name must be less than 100 characters")
    private String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    private String email;

    @Size(max = 15, message = "Phone number must be less than 15 characters")
    private String phoneNumber;

    @Size(max = 100, message = "Location must be less than 100 characters")
    private String location;

    private LocalDate dateOfBirth;

    @Size(max = 100, message = "Title must be less than 100 characters")
    private String title;

    @Size(max = 500, message = "Headline must be less than 500 characters")
    private String headline;

    private List<String> skills;
    private EducationDTO education;
    private ExperienceDTO experience;
    private JobPreferencesDTO jobPreferences;
    private NotificationPreferencesDTO notificationPreferences;
    private String resumePath;
    private String profilePicturePath;
    private Double profileCompleteness;
}

@Data
@NoArgsConstructor
@AllArgsConstructor
class EducationDTO {
    @Size(max = 100, message = "Degree must be less than 100 characters")
    private String degree;

    @Size(max = 100, message = "Institution must be less than 100 characters")
    private String institution;

    private Integer graduationYear;
}

@Data
@NoArgsConstructor
@AllArgsConstructor
class ExperienceDTO {
    private Boolean isExperienced = false;

    @Size(max = 100, message = "Role must be less than 100 characters")
    private String role;

    @Size(max = 100, message = "Company must be less than 100 characters")
    private String company;

    private Integer yearsOfExperience;
}

@Data
@NoArgsConstructor
@AllArgsConstructor
class JobPreferencesDTO {
    @Size(max = 50, message = "Job type must be less than 50 characters")
    private String jobType;

    @Size(max = 100, message = "Expected salary must be less than 100 characters")
    private String expectedSalary;

    private Boolean willingToRelocate = false;
}

@Data
@NoArgsConstructor
@AllArgsConstructor
class NotificationPreferencesDTO {
    private Boolean emailAlerts = true;
    private Boolean inAppNotifications = true;
}

