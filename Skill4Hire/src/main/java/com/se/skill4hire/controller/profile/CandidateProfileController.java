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

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public ResponseEntity<Map<String, String>> uploadResume(
            @RequestParam("resume") MultipartFile file,
            HttpSession session) {
        Long candidateId = (Long) session.getAttribute("userId");
        String fileName = candidateService.uploadResume(candidateId, file);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Resume uploaded successfully");
        response.put("fileName", fileName);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/upload/profile-picture")
    @PreAuthorize("hasAnyAuthority('CANDIDATE', 'ADMIN')")
    public ResponseEntity<Map<String, String>> uploadProfilePicture(
            @RequestParam("profilePicture") MultipartFile file,
            HttpSession session) {
        Long candidateId = (Long) session.getAttribute("userId");
        String fileName = candidateService.uploadProfilePicture(candidateId, file);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Profile picture uploaded successfully");
        response.put("fileName", fileName);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/skills")
    @PreAuthorize("hasAnyAuthority('CANDIDATE', 'ADMIN')")
    public ResponseEntity<List<String>> addSkill(
            @RequestParam("skill") String skill,  // Fixed parameter name
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

        // URL decode the skill parameter
        String decodedSkill = URLDecoder.decode(skill, StandardCharsets.UTF_8);

        List<String> updatedSkills = candidateService.removeSkill(candidateId, decodedSkill);
        return ResponseEntity.ok(updatedSkills);
    }
}