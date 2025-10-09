package com.se.skill4hire.service.profile;

import com.se.skill4hire.dto.profile.CandidateProfileDTO;
import com.se.skill4hire.dto.profile.ProfileCompletenessDTO;
import com.se.skill4hire.entity.auth.Candidate;
import com.se.skill4hire.entity.profile.*;
import com.se.skill4hire.dto.profile.EducationDTO;
import com.se.skill4hire.dto.profile.ExperienceDTO;
import com.se.skill4hire.dto.profile.JobPreferencesDTO;
import com.se.skill4hire.dto.profile.NotificationPreferencesDTO;
import com.se.skill4hire.repository.auth.CandidateAuthRepository;
import com.se.skill4hire.repository.profile.CandidateProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CandidateServiceImpl implements CandidateService {

    @Autowired
    private CandidateProfileRepository candidateProfileRepository;

    private final CandidateAuthRepository candidateAuthRepository;
    private static final String UPLOAD_DIR = "uploads/";

    public CandidateServiceImpl(CandidateAuthRepository candidateAuthRepository) {
        this.candidateAuthRepository = candidateAuthRepository;
    }

    @Override
    public CandidateProfileDTO getProfile(String candidateId) {
        CandidateProfile profile = candidateProfileRepository.findByUserId(candidateId)
                .orElseThrow(() -> new RuntimeException("Candidate profile not found for user id: " + candidateId));
        return convertToDTO(profile);
    }

    @Override
    public CandidateProfileDTO updateProfile(String candidateId, CandidateProfileDTO profileDTO) {
        CandidateProfile profile = candidateProfileRepository.findByUserId(candidateId)
                .orElseGet(() -> createNewProfile(candidateId));

        // Update profile fields from DTO
        profile.setPhoneNumber(profileDTO.getPhoneNumber());
        profile.setLocation(profileDTO.getLocation());
        profile.setDateOfBirth(profileDTO.getDateOfBirth());
        profile.setTitle(profileDTO.getTitle());
        profile.setHeadline(profileDTO.getHeadline());
        profile.setSkills(profileDTO.getSkills());

        // Update embedded objects
        if (profileDTO.getEducation() != null) {
            if (profile.getEducation() == null) {
                profile.setEducation(new Education());
            }
            profile.getEducation().setDegree(profileDTO.getEducation().getDegree());
            profile.getEducation().setInstitution(profileDTO.getEducation().getInstitution());
            profile.getEducation().setGraduationYear(profileDTO.getEducation().getGraduationYear());
        }

        if (profileDTO.getExperience() != null) {
            if (profile.getExperience() == null) {
                profile.setExperience(new Experience());
            }
            profile.getExperience().setIsExperienced(profileDTO.getExperience().getIsExperienced());
            profile.getExperience().setRole(profileDTO.getExperience().getRole());
            profile.getExperience().setCompany(profileDTO.getExperience().getCompany());
            profile.getExperience().setYearsOfExperience(profileDTO.getExperience().getYearsOfExperience());
        }

        if (profileDTO.getJobPreferences() != null) {
            if (profile.getJobPreferences() == null) {
                profile.setJobPreferences(new JobPreferences());
            }
            profile.getJobPreferences().setPreferredRole(profileDTO.getJobPreferences().getPreferredRole());
            profile.getJobPreferences().setPreferredLocation(profileDTO.getJobPreferences().getPreferredLocation());
            profile.getJobPreferences().setExpectedSalary(profileDTO.getJobPreferences().getExpectedSalary());
            profile.getJobPreferences().setJobType(profileDTO.getJobPreferences().getJobType());
            profile.getJobPreferences().setWillingToRelocate(profileDTO.getJobPreferences().getWillingToRelocate());
        }

        if (profileDTO.getNotificationPreferences() != null) {
            if (profile.getNotificationPreferences() == null) {
                profile.setNotificationPreferences(new NotificationPreferences());
            }
            profile.getNotificationPreferences().setEmailNotifications(profileDTO.getNotificationPreferences().getEmailNotifications());
            profile.getNotificationPreferences().setSmsNotifications(profileDTO.getNotificationPreferences().getSmsNotifications());
            profile.getNotificationPreferences().setCustomPreferences(profileDTO.getNotificationPreferences().getCustomPreferences());
            profile.getNotificationPreferences().setEmailAlerts(profileDTO.getNotificationPreferences().getEmailAlerts());
            profile.getNotificationPreferences().setInAppNotifications(profileDTO.getNotificationPreferences().getInAppNotifications());
        }

        profile.setProfileCompleteness(calculateCompleteness(profile));
        CandidateProfile updatedProfile = candidateProfileRepository.save(profile);
        return convertToDTO(updatedProfile);
    }

    private CandidateProfile createNewProfile(String candidateId) {
        Candidate authCandidate = candidateAuthRepository.findById(candidateId)
                .orElseThrow(() -> new RuntimeException("Auth candidate not found with id: " + candidateId));

        CandidateProfile newProfile = new CandidateProfile();
        newProfile.setUserId(candidateId);
        newProfile.setName(authCandidate.getName() != null ? authCandidate.getName() : "");
        newProfile.setEmail(authCandidate.getEmail() != null ? authCandidate.getEmail() : "");
        return candidateProfileRepository.save(newProfile);
    }

    @Override
    public ProfileCompletenessDTO getProfileCompleteness(String candidateId) {
        CandidateProfile profile = candidateProfileRepository.findByUserId(candidateId)
                .orElseThrow(() -> new RuntimeException("Candidate profile not found for user id: " + candidateId));

        double completeness = calculateCompleteness(profile);
        String message = getCompletenessMessage(completeness);
        return new ProfileCompletenessDTO(completeness, message);
    }

    @Override
    public String uploadResume(String candidateId, MultipartFile file) {
        CandidateProfile profile = candidateProfileRepository.findByUserId(candidateId)
                .orElseGet(() -> createNewProfile(candidateId));

        try {
            String fileName = saveFile(file, "resumes");
            profile.setResumePath(fileName);
            profile.setProfileCompleteness(calculateCompleteness(profile));
            candidateProfileRepository.save(profile);
            return fileName;
        } catch (IOException e) {
            throw new RuntimeException("Failed to upload resume", e);
        }
    }

    @Override
    public String uploadProfilePicture(String candidateId, MultipartFile file) {
        CandidateProfile profile = candidateProfileRepository.findByUserId(candidateId)
                .orElseGet(() -> createNewProfile(candidateId));

        try {
            String fileName = saveFile(file, "profile-pictures");
            profile.setProfilePicturePath(fileName);
            profile.setProfileCompleteness(calculateCompleteness(profile));
            candidateProfileRepository.save(profile);
            return fileName;
        } catch (IOException e) {
            throw new RuntimeException("Failed to upload profile picture", e);
        }
    }

    @Override
    public List<String> addSkill(String candidateId, String skill) {
        CandidateProfile profile = candidateProfileRepository.findByUserId(candidateId)
                .orElseGet(() -> createNewProfile(candidateId));

        if (!profile.getSkills().contains(skill)) {
            profile.getSkills().add(skill);
            profile.setProfileCompleteness(calculateCompleteness(profile));
            candidateProfileRepository.save(profile);
        }

        return profile.getSkills();
    }

    @Override
    public List<String> removeSkill(String candidateId, String skill) {
        CandidateProfile profile = candidateProfileRepository.findByUserId(candidateId)
                .orElseThrow(() -> new RuntimeException("Candidate profile not found for user id: " + candidateId));

        profile.getSkills().remove(skill);
        profile.setProfileCompleteness(calculateCompleteness(profile));
        candidateProfileRepository.save(profile);

        return profile.getSkills();
    }

    @Override
    public CandidateProfile getCandidateEntity(String candidateId) {
        return candidateProfileRepository.findByUserId(candidateId)
                .orElseThrow(() -> new RuntimeException("Candidate profile not found for user id: " + candidateId));
    }

    private CandidateProfileDTO convertToDTO(CandidateProfile profile) {
        CandidateProfileDTO dto = new CandidateProfileDTO();

        // Get basic info from auth user
        Candidate authCandidate = candidateAuthRepository.findById(profile.getUserId()).orElse(null);
        if (authCandidate != null) {
            dto.setName(authCandidate.getName());
            dto.setEmail(authCandidate.getEmail());
        }

        // Copy profile-specific fields
        dto.setId(profile.getId());
        dto.setPhoneNumber(profile.getPhoneNumber());
        dto.setLocation(profile.getLocation());
        dto.setDateOfBirth(profile.getDateOfBirth());
        dto.setTitle(profile.getTitle());
        dto.setHeadline(profile.getHeadline());
        dto.setSkills(profile.getSkills());
        dto.setResumePath(profile.getResumePath());
        dto.setProfilePicturePath(profile.getProfilePicturePath());
        dto.setProfileCompleteness(profile.getProfileCompleteness());
        dto.setCreatedAt(profile.getCreatedAt());
        dto.setUpdatedAt(profile.getUpdatedAt());

        // Convert embedded objects to DTOs
        if (profile.getEducation() != null) {
            EducationDTO educationDTO = new EducationDTO();
            educationDTO.setDegree(profile.getEducation().getDegree());
            educationDTO.setInstitution(profile.getEducation().getInstitution());
            educationDTO.setGraduationYear(profile.getEducation().getGraduationYear());
            dto.setEducation(educationDTO);
        }

        if (profile.getExperience() != null) {
            ExperienceDTO experienceDTO = new ExperienceDTO();
            experienceDTO.setIsExperienced(profile.getExperience().getIsExperienced());
            experienceDTO.setRole(profile.getExperience().getRole());
            experienceDTO.setCompany(profile.getExperience().getCompany());
            experienceDTO.setYearsOfExperience(profile.getExperience().getYearsOfExperience());
            dto.setExperience(experienceDTO);
        }

        if (profile.getJobPreferences() != null) {
            JobPreferencesDTO jobPrefDTO = new JobPreferencesDTO();
            jobPrefDTO.setPreferredRole(profile.getJobPreferences().getPreferredRole());
            jobPrefDTO.setPreferredLocation(profile.getJobPreferences().getPreferredLocation());
            jobPrefDTO.setExpectedSalary(profile.getJobPreferences().getExpectedSalary());
            jobPrefDTO.setJobType(profile.getJobPreferences().getJobType());
            jobPrefDTO.setWillingToRelocate(profile.getJobPreferences().getWillingToRelocate());
            dto.setJobPreferences(jobPrefDTO);
        }

        if (profile.getNotificationPreferences() != null) {
            NotificationPreferencesDTO notifPrefDTO = new NotificationPreferencesDTO();
            notifPrefDTO.setEmailNotifications(profile.getNotificationPreferences().getEmailNotifications());
            notifPrefDTO.setSmsNotifications(profile.getNotificationPreferences().getSmsNotifications());
            notifPrefDTO.setCustomPreferences(profile.getNotificationPreferences().getCustomPreferences());
            notifPrefDTO.setEmailAlerts(profile.getNotificationPreferences().getEmailAlerts());
            notifPrefDTO.setInAppNotifications(profile.getNotificationPreferences().getInAppNotifications());
            dto.setNotificationPreferences(notifPrefDTO);
        }

        return dto;
    }

    private double calculateCompleteness(CandidateProfile profile) {
        int totalFields = 10;
        int completedFields = 0;

        // Check auth user fields
        Candidate authCandidate = candidateAuthRepository.findById(profile.getUserId()).orElse(null);
        if (authCandidate != null) {
            if (authCandidate.getName() != null && !authCandidate.getName().isEmpty()) completedFields++;
            if (authCandidate.getEmail() != null && !authCandidate.getEmail().isEmpty()) completedFields++;
        }

        // Check profile fields
        if (profile.getTitle() != null && !profile.getTitle().isEmpty()) completedFields++;
        if (profile.getSkills() != null && !profile.getSkills().isEmpty()) completedFields++;

        if (profile.getEducation() != null && profile.getEducation().getDegree() != null &&
                !profile.getEducation().getDegree().isEmpty()) completedFields++;

        if (profile.getExperience() != null && profile.getExperience().getRole() != null &&
                !profile.getExperience().getRole().isEmpty()) completedFields++;

        if (profile.getResumePath() != null && !profile.getResumePath().isEmpty()) completedFields++;
        if (profile.getLocation() != null && !profile.getLocation().isEmpty()) completedFields++;

        if (profile.getJobPreferences() != null && profile.getJobPreferences().getJobType() != null &&
                !profile.getJobPreferences().getJobType().isEmpty()) completedFields++;

        if (profile.getProfilePicturePath() != null && !profile.getProfilePicturePath().isEmpty()) completedFields++;

        return (double) completedFields / totalFields * 100;
    }

    private String getCompletenessMessage(double completeness) {
        if (completeness < 50) {
            return "Complete your profile to improve job matches. " + (100 - Math.round(completeness)) + "% remaining.";
        } else if (completeness < 100) {
            return "Your profile is looking good! " + (100 - Math.round(completeness)) + "% remaining to complete.";
        } else {
            return "Your profile is complete! Great job!";
        }
    }

    private String saveFile(MultipartFile file, String subdirectory) throws IOException {
        String originalFileName = file.getOriginalFilename();
        String fileExtension = originalFileName != null ?
                originalFileName.substring(originalFileName.lastIndexOf(".")) : "";
        String fileName = UUID.randomUUID().toString() + fileExtension;

        Path uploadPath = Paths.get(UPLOAD_DIR + subdirectory);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        Path filePath = uploadPath.resolve(fileName);
        Files.copy(file.getInputStream(), filePath);

        return fileName;
    }
}
