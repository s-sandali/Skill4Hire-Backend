package com.se.skill4hire.service.profile;

import com.se.skill4hire.dto.profile.EmployeeProfileDTO;
import com.se.skill4hire.dto.profile.ProfileCompletenessDTO;
import org.springframework.web.multipart.MultipartFile;

public interface EmployeeProfileService {
    EmployeeProfileDTO getProfile(String employeeId);
    EmployeeProfileDTO updateProfile(String employeeId, EmployeeProfileDTO profileDTO);
    ProfileCompletenessDTO getProfileCompleteness(String employeeId);
    String uploadProfilePicture(String employeeId, MultipartFile file);
}