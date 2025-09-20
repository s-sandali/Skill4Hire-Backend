package com.se.skill4hire.controller.profile;

import com.se.skill4hire.dto.profile.CandidateProfileDTO;
import com.se.skill4hire.dto.profile.ProfileCompletenessDTO;
import com.se.skill4hire.service.profile.CandidateService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/candidates")
@RequiredArgsConstructor
public class CandidateProfileController {

    private final CandidateService candidateService;

    @GetMapping("/profile")
    @PreAuthorize("hasAnyAuthority('CANDIDATE', 'ADMIN')")
    public ResponseEntity<CandidateProfileDTO> getProfile(HttpSession session) {
        Long candidateId = (Long) session.getAttribute("userId");
        CandidateProfileDTO profile = candidateService.getProfile(candidateId);
        return ResponseEntity.ok(profile);
    }

    @PutMapping("/profile")
    @PreAuthorize("hasAnyAuthority('CANDIDATE', 'ADMIN')")
    public ResponseEntity<CandidateProfileDTO> updateProfile(
            @Valid @RequestBody CandidateProfileDTO profileDTO,
            HttpSession session) {
        Long candidateId = (Long) session.getAttribute("userId");
        CandidateProfileDTO updatedProfile = candidateService.updateProfile(candidateId, profileDTO);
        return ResponseEntity.ok(updatedProfile);
    }

    @GetMapping("/profile/completeness")
    @PreAuthorize("hasAnyAuthority('CANDIDATE', 'ADMIN')")
    public ResponseEntity<ProfileCompletenessDTO> getProfileCompleteness(HttpSession session) {
        Long candidateId = (Long) session.getAttribute("userId");
        ProfileCompletenessDTO completeness = candidateService.getProfileCompleteness(candidateId);
        return ResponseEntity.ok(completeness);
    }

    @PostMapping("/upload/resume")
    @PreAuthorize("hasAnyAuthority('CANDIDATE', 'ADMIN')")
    public ResponseEntity<String> uploadResume(
            @RequestParam("resume") MultipartFile file,
            HttpSession session) {
        Long candidateId = (Long) session.getAttribute("userId");
        String fileName = candidateService.uploadResume(candidateId, file);
        return ResponseEntity.ok("Resume uploaded successfully: " + fileName);
    }

    @PostMapping("/upload/profile-picture")
    @PreAuthorize("hasAnyAuthority('CANDIDATE', 'ADMIN')")
    public ResponseEntity<String> uploadProfilePicture(
            @RequestParam("profilePicture") MultipartFile file,
            HttpSession session) {
        Long candidateId = (Long) session.getAttribute("userId");
        String fileName = candidateService.uploadProfilePicture(candidateId, file);
        return ResponseEntity.ok("Profile picture uploaded successfully: " + fileName);
    }

    @PostMapping("/skills")
    @PreAuthorize("hasAnyAuthority('CANDIDATE', 'ADMIN')")
    public ResponseEntity<List<String>> addSkill(
            @RequestParam String skill,
            HttpSession session) {
        Long candidateId = (Long) session.getAttribute("userId");
        List<String> updatedSkills = candidateService.addSkill(candidateId, skill);
        return ResponseEntity.ok(updatedSkills);
    }

    @DeleteMapping("/skills/{skill}")
    @PreAuthorize("hasAnyAuthority('CANDIDATE', 'ADMIN')")
    public ResponseEntity<List<String>> removeSkill(
            @PathVariable String skill,
            HttpSession session) {
        Long candidateId = (Long) session.getAttribute("userId");
        List<String> updatedSkills = candidateService.removeSkill(candidateId, skill);
        return ResponseEntity.ok(updatedSkills);
    }
}