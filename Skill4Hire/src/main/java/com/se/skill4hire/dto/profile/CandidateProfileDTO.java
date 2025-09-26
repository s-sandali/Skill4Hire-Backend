package com.se.skill4hire.dto.profile;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CandidateProfileDTO {
    private Long id;
    private String name;
    private String email;
    private String phoneNumber;
    private String location;
    private LocalDate dateOfBirth;
    private String title;
    private String headline;
    private List<String> skills;
    private EducationDTO education;
    private ExperienceDTO experience;
    private JobPreferencesDTO jobPreferences;
    private NotificationPreferencesDTO notificationPreferences;
    private String resumePath;
    private String profilePicturePath;
    private Double profileCompleteness;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}