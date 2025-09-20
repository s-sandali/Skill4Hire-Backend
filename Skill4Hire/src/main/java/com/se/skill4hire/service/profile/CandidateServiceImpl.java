package com.se.skill4hire.service.profile;

import com.se.skill4hire.dto.profile.CandidateProfileDTO;
import com.se.skill4hire.dto.profile.ProfileCompletenessDTO;
import com.se.skill4hire.entity.profile.CandidateProfile;
import com.se.skill4hire.repository.profile.CandidateProfileRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CandidateServiceImpl implements CandidateService {

    private final CandidateProfileRepository candidateProfileRepository;
    private static final String UPLOAD_DIR = "uploads/";

    @Override
    public CandidateProfileDTO getProfile(Long candidateId) {
        CandidateProfile candidate = candidateProfileRepository.findById(candidateId)
                .orElseThrow(() -> new EntityNotFoundException("Candidate not found with id: " + candidateId));
        return convertToDTO(candidate);
    }

    @Override
    public CandidateProfileDTO updateProfile(Long candidateId, CandidateProfileDTO profileDTO) {
        CandidateProfile candidate = candidateProfileRepository.findById(candidateId)
                .orElseThrow(() -> new EntityNotFoundException("Candidate not found with id: " + candidateId));

        // Update fields from DTO
        BeanUtils.copyProperties(profileDTO, candidate, "id", "user", "resumePath", "profilePicturePath");

        // Calculate profile completeness
        candidate.setProfileCompleteness(calculateCompleteness(candidate));

        CandidateProfile updatedCandidate = candidateProfileRepository.save(candidate);
        return convertToDTO(updatedCandidate);
    }

    @Override
    public ProfileCompletenessDTO getProfileCompleteness(Long candidateId) {
        CandidateProfile candidate = candidateProfileRepository.findById(candidateId)
                .orElseThrow(() -> new EntityNotFoundException("Candidate not found with id: " + candidateId));

        double completeness = calculateCompleteness(candidate);
        String message = getCompletenessMessage(completeness);

        return new ProfileCompletenessDTO(completeness, message);
    }

    @Override
    public String uploadResume(Long candidateId, MultipartFile file) {
        CandidateProfile candidate = candidateProfileRepository.findById(candidateId)
                .orElseThrow(() -> new EntityNotFoundException("Candidate not found with id: " + candidateId));

        try {
            String fileName = saveFile(file, "resumes");
            candidate.setResumePath(fileName);
            candidateProfileRepository.save(candidate);
            return fileName;
        } catch (IOException e) {
            throw new RuntimeException("Failed to upload resume", e);
        }
    }

    @Override
    public String uploadProfilePicture(Long candidateId, MultipartFile file) {
        CandidateProfile candidate = candidateProfileRepository.findById(candidateId)
                .orElseThrow(() -> new EntityNotFoundException("Candidate not found with id: " + candidateId));

        try {
            String fileName = saveFile(file, "profile-pictures");
            candidate.setProfilePicturePath(fileName);
            candidateProfileRepository.save(candidate);
            return fileName;
        } catch (IOException e) {
            throw new RuntimeException("Failed to upload profile picture", e);
        }
    }

    @Override
    public List<String> addSkill(Long candidateId, String skill) {
        CandidateProfile candidate = candidateProfileRepository.findById(candidateId)
                .orElseThrow(() -> new EntityNotFoundException("Candidate not found with id: " + candidateId));

        if (!candidate.getSkills().contains(skill)) {
            candidate.getSkills().add(skill);
            candidateProfileRepository.save(candidate);
        }

        return candidate.getSkills();
    }

    @Override
    public List<String> removeSkill(Long candidateId, String skill) {
        CandidateProfile candidate = candidateProfileRepository.findById(candidateId)
                .orElseThrow(() -> new EntityNotFoundException("Candidate not found with id: " + candidateId));

        candidate.getSkills().remove(skill);
        candidateProfileRepository.save(candidate);

        return candidate.getSkills();
    }

    @Override
    public CandidateProfile getCandidateEntity(Long candidateId) {
        return candidateProfileRepository.findById(candidateId)
                .orElseThrow(() -> new EntityNotFoundException("Candidate not found with id: " + candidateId));
    }

    private CandidateProfileDTO convertToDTO(CandidateProfile candidate) {
        CandidateProfileDTO dto = new CandidateProfileDTO();
        BeanUtils.copyProperties(candidate, dto);
        return dto;
    }

    private double calculateCompleteness(CandidateProfile candidate) {
        int totalFields = 10;
        int completedFields = 0;

        if (candidate.getName() != null && !candidate.getName().isEmpty()) completedFields++;
        if (candidate.getEmail() != null && !candidate.getEmail().isEmpty()) completedFields++;
        if (candidate.getTitle() != null && !candidate.getTitle().isEmpty()) completedFields++;
        if (candidate.getSkills() != null && !candidate.getSkills().isEmpty()) completedFields++;
        if (candidate.getEducation() != null && candidate.getEducation().getDegree() != null) completedFields++;
        if (candidate.getExperience() != null && candidate.getExperience().getRole() != null) completedFields++;
        if (candidate.getResumePath() != null && !candidate.getResumePath().isEmpty()) completedFields++;
        if (candidate.getLocation() != null && !candidate.getLocation().isEmpty()) completedFields++;
        if (candidate.getJobPreferences() != null && candidate.getJobPreferences().getJobType() != null) completedFields++;
        if (candidate.getProfilePicturePath() != null && !candidate.getProfilePicturePath().isEmpty()) completedFields++;

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