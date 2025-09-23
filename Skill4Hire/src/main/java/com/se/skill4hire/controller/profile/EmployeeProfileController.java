package com.se.skill4hire.controller.profile;

import com.se.skill4hire.dto.profile.EmployeeProfileDTO;
import com.se.skill4hire.entity.EmployeeProfile;
import com.se.skill4hire.service.profile.EmployeeProfileService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/employees")
@RequiredArgsConstructor
public class EmployeeProfileController {

    private final EmployeeProfileService employeeProfileService;

    @PostMapping("/profile")
    @PreAuthorize("hasAnyAuthority('EMPLOYEE', 'ADMIN')")
    public ResponseEntity<EmployeeProfileDTO> createProfile(
            @Valid @RequestBody EmployeeProfileDTO profileDTO,
            HttpSession session) {
        EmployeeProfileDTO createdProfile = employeeProfileService.createEmployeeProfile(profileDTO);
        return ResponseEntity.ok(createdProfile);
    }

    @GetMapping("/profile")
    @PreAuthorize("hasAnyAuthority('EMPLOYEE', 'ADMIN')")
    public ResponseEntity<EmployeeProfileDTO> getProfile(HttpSession session) {
        Long employeeId = (Long) session.getAttribute("userId");
        EmployeeProfileDTO profile = employeeProfileService.getEmployeeProfileById(employeeId);
        return ResponseEntity.ok(profile);
    }

    @GetMapping("/profile/{id}")
    @PreAuthorize("hasAnyAuthority('EMPLOYEE', 'ADMIN')")
    public ResponseEntity<EmployeeProfileDTO> getProfileById(@PathVariable Long id) {
        EmployeeProfileDTO profile = employeeProfileService.getEmployeeProfileById(id);
        return ResponseEntity.ok(profile);
    }

    @GetMapping("/profile/email/{email}")
    @PreAuthorize("hasAnyAuthority('EMPLOYEE', 'ADMIN')")
    public ResponseEntity<EmployeeProfileDTO> getProfileByEmail(@PathVariable String email) {
        EmployeeProfileDTO profile = employeeProfileService.getEmployeeProfileByEmail(email);
        return ResponseEntity.ok(profile);
    }

    @GetMapping("/profile/full/{id}")
    @PreAuthorize("hasAnyAuthority('EMPLOYEE', 'ADMIN')")
    public ResponseEntity<EmployeeProfile> getFullProfile(@PathVariable Long id) {
        EmployeeProfile profile = employeeProfileService.getFullEmployeeProfile(id);
        return ResponseEntity.ok(profile);
    }

    @PutMapping("/profile")
    @PreAuthorize("hasAnyAuthority('EMPLOYEE', 'ADMIN')")
    public ResponseEntity<EmployeeProfileDTO> updateProfile(
            @Valid @RequestBody EmployeeProfileDTO profileDTO,
            HttpSession session) {
        Long employeeId = (Long) session.getAttribute("userId");
        EmployeeProfileDTO updatedProfile = employeeProfileService.updateEmployeeProfile(employeeId, profileDTO);
        return ResponseEntity.ok(updatedProfile);
    }

    @PutMapping("/profile/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<EmployeeProfileDTO> updateProfileById(
            @PathVariable Long id,
            @Valid @RequestBody EmployeeProfileDTO profileDTO) {
        EmployeeProfileDTO updatedProfile = employeeProfileService.updateEmployeeProfile(id, profileDTO);
        return ResponseEntity.ok(updatedProfile);
    }

    @PutMapping("/profile/phone")
    @PreAuthorize("hasAnyAuthority('EMPLOYEE', 'ADMIN')")
    public ResponseEntity<Map<String, String>> updatePhone(
            @RequestParam String phone,
            HttpSession session) {
        Long employeeId = (Long) session.getAttribute("userId");
        employeeProfileService.updateEmployeePhone(employeeId, phone);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Phone number updated successfully");
        response.put("phone", phone);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/profile/position")
    @PreAuthorize("hasAnyAuthority('EMPLOYEE', 'ADMIN')")
    public ResponseEntity<Map<String, String>> updatePosition(
            @RequestParam String position,
            HttpSession session) {
        Long employeeId = (Long) session.getAttribute("userId");
        employeeProfileService.updateEmployeePosition(employeeId, position);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Position updated successfully");
        response.put("position", position);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/profiles")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<List<EmployeeProfile>> getAllProfiles() {
        List<EmployeeProfile> profiles = employeeProfileService.getAllEmployeeProfiles();
        return ResponseEntity.ok(profiles);
    }

    @GetMapping("/profiles/active")
    @PreAuthorize("hasAnyAuthority('EMPLOYEE', 'ADMIN')")
    public ResponseEntity<List<EmployeeProfile>> getActiveProfiles() {
        List<EmployeeProfile> profiles = employeeProfileService.getAllActiveEmployeeProfiles();
        return ResponseEntity.ok(profiles);
    }

    @DeleteMapping("/profile")
    @PreAuthorize("hasAnyAuthority('EMPLOYEE', 'ADMIN')")
    public ResponseEntity<Map<String, String>> deactivateProfile(HttpSession session) {
        Long employeeId = (Long) session.getAttribute("userId");
        employeeProfileService.deactivateEmployeeProfile(employeeId);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Profile deactivated successfully");
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/profile/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<Map<String, String>> deactivateProfileById(@PathVariable Long id) {
        employeeProfileService.deactivateEmployeeProfile(id);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Profile deactivated successfully");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/check-email")
    public ResponseEntity<Map<String, Boolean>> checkEmailExists(@RequestParam String email) {
        boolean exists = employeeProfileService.emailExists(email);

        Map<String, Boolean> response = new HashMap<>();
        response.put("exists", exists);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/profile/me")
    @PreAuthorize("hasAnyAuthority('EMPLOYEE', 'ADMIN')")
    public ResponseEntity<EmployeeProfileDTO> getMyProfile(HttpSession session) {
        Long employeeId = (Long) session.getAttribute("userId");
        EmployeeProfileDTO profile = employeeProfileService.getEmployeeProfileById(employeeId);
        return ResponseEntity.ok(profile);
    }

    @PatchMapping("/profile/activate/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<Map<String, String>> activateProfile(@PathVariable Long id) {
        // Note: You'll need to add an activate method in your service
        // For now, this is a placeholder
        Map<String, String> response = new HashMap<>();
        response.put("message", "Profile activation endpoint - implement in service");
        return ResponseEntity.ok(response);
    }
}