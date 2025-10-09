package com.se.skill4hire.service.profile;

import com.se.skill4hire.dto.profile.CandidateProfileDTO;
import com.se.skill4hire.dto.profile.ProfileCompletenessDTO;
import com.se.skill4hire.entity.profile.CandidateProfile;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface CandidateService {
    CandidateProfileDTO getProfile(String candidateId);
    CandidateProfileDTO updateProfile(String candidateId, CandidateProfileDTO profileDTO);
    ProfileCompletenessDTO getProfileCompleteness(String candidateId);
    String uploadResume(String candidateId, MultipartFile file);
    String uploadProfilePicture(String candidateId, MultipartFile file);
    List<String> addSkill(String candidateId, String skill);
    List<String> removeSkill(String candidateId, String skill);
    CandidateProfile getCandidateEntity(String candidateId);
}