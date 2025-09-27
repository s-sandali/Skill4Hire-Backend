package com.se.skill4hire.controller.profile;

import com.se.skill4hire.dto.profile.EmployeeProfileDTO;
import com.se.skill4hire.dto.profile.ProfileCompletenessDTO;
import com.se.skill4hire.service.profile.EmployeeProfileService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/employees")
@RequiredArgsConstructor
public class EmployeeProfileController {

    private final EmployeeProfileService employeeProfileService;

    @GetMapping("/profile")
    @PreAuthorize("hasAnyAuthority('EMPLOYEE', 'ADMIN')")
    public ResponseEntity<EmployeeProfileDTO> getProfile(HttpSession session) {
        Long employeeId = (Long) session.getAttribute("userId");
        EmployeeProfileDTO profile = employeeProfileService.getProfile(employeeId);
        return ResponseEntity.ok(profile);
    }

    @PutMapping("/profile")
    @PreAuthorize("hasAnyAuthority('EMPLOYEE', 'ADMIN')")
    public ResponseEntity<EmployeeProfileDTO> updateProfile(
            @Valid @RequestBody EmployeeProfileDTO profileDTO,
            HttpSession session) {
        Long employeeId = (Long) session.getAttribute("userId");
        EmployeeProfileDTO updatedProfile = employeeProfileService.updateProfile(employeeId, profileDTO);
        return ResponseEntity.ok(updatedProfile);
    }

    @GetMapping("/profile/completeness")
    @PreAuthorize("hasAnyAuthority('EMPLOYEE', 'ADMIN')")
    public ResponseEntity<ProfileCompletenessDTO> getProfileCompleteness(HttpSession session) {
        Long employeeId = (Long) session.getAttribute("userId");
        ProfileCompletenessDTO completeness = employeeProfileService.getProfileCompleteness(employeeId);
        return ResponseEntity.ok(completeness);
    }

    @PostMapping("/upload/profile-picture")
    @PreAuthorize("hasAnyAuthority('EMPLOYEE', 'ADMIN')")
    public ResponseEntity<Map<String, String>> uploadProfilePicture(
            @RequestParam("profilePicture") MultipartFile file,
            HttpSession session) {
        Long employeeId = (Long) session.getAttribute("userId");
        String fileName = employeeProfileService.uploadProfilePicture(employeeId, file);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Profile picture uploaded successfully");
        response.put("fileName", fileName);

        return ResponseEntity.ok(response);
    }
}