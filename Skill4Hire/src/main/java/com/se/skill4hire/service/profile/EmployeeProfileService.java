package com.se.skill4hire.service.profile;

import com.se.skill4hire.dto.profile.EmployeeProfileDTO;
import com.se.skill4hire.dto.profile.ProfileCompletenessDTO;
import org.springframework.web.multipart.MultipartFile;

public interface EmployeeProfileService {
    EmployeeProfileDTO getProfile(Long employeeId);
    EmployeeProfileDTO updateProfile(Long employeeId, EmployeeProfileDTO profileDTO);
    ProfileCompletenessDTO getProfileCompleteness(Long employeeId);
    String uploadProfilePicture(Long employeeId, MultipartFile file);
}