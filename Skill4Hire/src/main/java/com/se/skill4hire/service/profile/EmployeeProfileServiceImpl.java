package com.se.skill4hire.service.profile;

import com.se.skill4hire.dto.profile.EmployeeProfileDTO;
import com.se.skill4hire.dto.profile.ProfileCompletenessDTO;
import com.se.skill4hire.entity.auth.Employee;
import com.se.skill4hire.entity.EmployeeProfile;
import com.se.skill4hire.entity.job.JobPost;
import com.se.skill4hire.entity.profile.CandidateProfile;
import com.se.skill4hire.repository.auth.EmployeeRepository;
import com.se.skill4hire.repository.profile.EmployeeProfileRepository;
import com.se.skill4hire.repository.profile.CandidateProfileRepository;
import com.se.skill4hire.repository.job.JobPostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
public class EmployeeProfileServiceImpl implements EmployeeProfileService {

    @Autowired
    private EmployeeProfileRepository employeeProfileRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private CandidateProfileRepository candidateProfileRepository;

    @Autowired
    private JobPostRepository jobPostRepository;

    private static final String UPLOAD_DIR = "uploads/";

    @Override
    public EmployeeProfileDTO getProfile(String employeeId) {
        EmployeeProfile profile = employeeProfileRepository.findByUserId(employeeId)
                .orElseThrow(() -> new NoSuchElementException("Employee profile not found for user id: " + employeeId));
        return convertToDTO(profile);
    }

    @Override
    public EmployeeProfileDTO updateProfile(String employeeId, EmployeeProfileDTO profileDTO) {
        EmployeeProfile profile = employeeProfileRepository.findByUserId(employeeId)
                .orElseGet(() -> createNewProfile(employeeId));

        // Update profile fields from DTO
        profile.setName(profileDTO.getName());
        profile.setEmail(profileDTO.getEmail());
        profile.setPhoneNumber(profileDTO.getPhoneNumber());
        profile.setLocation(profileDTO.getLocation());
        profile.setDateOfBirth(profileDTO.getDateOfBirth());
        profile.setTitle(profileDTO.getTitle());
        profile.setHeadline(profileDTO.getHeadline());
        profile.setSkills(profileDTO.getSkills());
        profile.setEducation(profileDTO.getEducation() != null ? profileDTO.getEducation().toString() : null);
        profile.setExperience(profileDTO.getExperience() != null ? profileDTO.getExperience().toString() : null);
        profile.setNotificationPreferences(profileDTO.getNotificationPreferences() != null ? profileDTO.getNotificationPreferences().toString() : null);
        profile.setProfilePicturePath(profileDTO.getProfilePicturePath());
        profile.setProfileCompleteness(calculateCompleteness(profile));

        EmployeeProfile updatedProfile = employeeProfileRepository.save(profile);
        return convertToDTO(updatedProfile);
    }

    @Override
    public ProfileCompletenessDTO getProfileCompleteness(String employeeId) {
        EmployeeProfile profile = employeeProfileRepository.findByUserId(employeeId)
                .orElseThrow(() -> new NoSuchElementException("Employee profile not found for user id: " + employeeId));

        double completeness = calculateCompleteness(profile);
        String message = getCompletenessMessage(completeness);
        return new ProfileCompletenessDTO(completeness, message);
    }

    @Override
    public String uploadProfilePicture(String employeeId, MultipartFile file) {
        EmployeeProfile profile = employeeProfileRepository.findByUserId(employeeId)
                .orElseGet(() -> createNewProfile(employeeId));

        try {
            String fileName = saveFile(file, "profile-pictures");
            profile.setProfilePicturePath(fileName);
            profile.setProfileCompleteness(calculateCompleteness(profile));
            employeeProfileRepository.save(profile);
            return fileName;
        } catch (IOException e) {
            throw new RuntimeException("Failed to upload profile picture", e);
        }
    }

    @Override
    public long getTotalCandidatesCount() {
        try {
            return candidateProfileRepository.count();
        } catch (Exception e) {
            // Fallback if repository is not available
            return 0L;
        }
    }

    @Override
    public long getActiveJobsCount() {
        try {
            // Use the existing findByStatus method and get the size
            List<JobPost> activeJobs = jobPostRepository.findByStatus(JobPost.JobStatus.ACTIVE);
            return activeJobs != null ? activeJobs.size() : 0L;
        } catch (Exception e) {
            // Fallback if repository is not available
            return 0L;
        }
    }

    @Override
    public long getUpcomingInterviewsCount(String employeeId) {
        // This would typically query an interview repository
        // For now, return a mock value or 0
        try {
            // If you have an interview repository:
            // return interviewRepository.countByEmployeeIdAndDateAfter(employeeId, LocalDateTime.now());
            return 0L; // Temporary - implement based on your interview system
        } catch (Exception e) {
            return 0L;
        }
    }

    @Override
    public long getNewApplicationsCount() {
        // This would typically query applications from the last 7 days
        // For now, return a mock value or 0
        try {
            // If you have an application repository:
            // LocalDateTime weekAgo = LocalDateTime.now().minusDays(7);
            // return applicationRepository.countByCreatedAtAfter(weekAgo);
            return 0L; // Temporary - implement based on your application system
        } catch (Exception e) {
            return 0L;
        }
    }

    private EmployeeProfile createNewProfile(String employeeId) {
        Employee authEmployee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new NoSuchElementException("Auth employee not found with id: " + employeeId));

        EmployeeProfile newProfile = new EmployeeProfile();
        newProfile.setUserId(employeeId);
        newProfile.setName(authEmployee.getName() != null ? authEmployee.getName() : "");
        newProfile.setEmail(authEmployee.getEmail() != null ? authEmployee.getEmail() : "");
        return employeeProfileRepository.save(newProfile);
    }

    private EmployeeProfileDTO convertToDTO(EmployeeProfile profile) {
        EmployeeProfileDTO dto = new EmployeeProfileDTO();
        dto.setId(profile.getId());
        dto.setName(profile.getName());
        dto.setEmail(profile.getEmail());
        dto.setPhoneNumber(profile.getPhoneNumber());
        dto.setLocation(profile.getLocation());
        dto.setDateOfBirth(profile.getDateOfBirth());
        dto.setTitle(profile.getTitle());
        dto.setHeadline(profile.getHeadline());
        dto.setSkills(profile.getSkills());
        dto.setProfilePicturePath(profile.getProfilePicturePath());
        dto.setProfileCompleteness(profile.getProfileCompleteness());
        dto.setCreatedAt(profile.getCreatedAt());
        dto.setUpdatedAt(profile.getUpdatedAt());
        return dto;
    }

    private double calculateCompleteness(EmployeeProfile profile) {
        int totalFields = 10;
        int completedFields = 0;

        if (profile.getName() != null && !profile.getName().isEmpty()) completedFields++;
        if (profile.getEmail() != null && !profile.getEmail().isEmpty()) completedFields++;
        if (profile.getTitle() != null && !profile.getTitle().isEmpty()) completedFields++;
        if (profile.getHeadline() != null && !profile.getHeadline().isEmpty()) completedFields++;
        if (profile.getSkills() != null && !profile.getSkills().isEmpty()) completedFields++;
        if (profile.getLocation() != null && !profile.getLocation().isEmpty()) completedFields++;
        if (profile.getPhoneNumber() != null && !profile.getPhoneNumber().isEmpty()) completedFields++;
        if (profile.getExperience() != null && !profile.getExperience().isEmpty()) completedFields++;
        if (profile.getEducation() != null && !profile.getEducation().isEmpty()) completedFields++;
        if (profile.getProfilePicturePath() != null && !profile.getProfilePicturePath().isEmpty()) completedFields++;

        return (double) completedFields / totalFields * 100;
    }

    private String getCompletenessMessage(double completeness) {
        if (completeness < 50) {
            return "Complete your profile to attract better candidates. " + (100 - Math.round(completeness)) + "% remaining.";
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