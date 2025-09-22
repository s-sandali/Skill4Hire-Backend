package com.se.skill4hire.service.profile;

import com.se.skill4hire.dto.profile.CandidateProfileDTO;
import com.se.skill4hire.dto.profile.ProfileCompletenessDTO;
import com.se.skill4hire.entity.profile.CandidateProfile;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface CandidateService {
    CandidateProfileDTO getProfile(Long candidateId);
    CandidateProfileDTO updateProfile(Long candidateId, CandidateProfileDTO profileDTO);
    ProfileCompletenessDTO getProfileCompleteness(Long candidateId);
    String uploadResume(Long candidateId, MultipartFile file);
    String uploadProfilePicture(Long candidateId, MultipartFile file);
    List<String> addSkill(Long candidateId, String skill);
    List<String> removeSkill(Long candidateId, String skill);
    CandidateProfile getCandidateEntity(Long candidateId);
}