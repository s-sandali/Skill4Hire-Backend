package com.se.skill4hire.controller.profile;

import com.se.skill4hire.dto.profile.CompanyProfileDTO;
import com.se.skill4hire.service.profile.CompanyProfileService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/companies") // updated path
@RequiredArgsConstructor
public class CompanyProfileController {

    private final CompanyProfileService companyService; // consistent service naming

    @GetMapping("/profile")
    @PreAuthorize("hasAnyAuthority('COMPANY', 'ADMIN')")
    public ResponseEntity<CompanyProfileDTO> getProfile(HttpSession session) {
        Long companyId = (Long) session.getAttribute("userId");
        CompanyProfileDTO profile = companyService.getProfile(companyId);
        return ResponseEntity.ok(profile);
    }

    @PutMapping("/profile")
    @PreAuthorize("hasAnyAuthority('COMPANY', 'ADMIN')")
    public ResponseEntity<CompanyProfileDTO> updateProfile(@RequestBody CompanyProfileDTO dto,
                                                           HttpSession session) {
        Long companyId = (Long) session.getAttribute("userId");
        CompanyProfileDTO updatedProfile = companyService.updateProfile(companyId, dto);
        return ResponseEntity.ok(updatedProfile);
    }

    @PostMapping("/upload/logo")
    @PreAuthorize("hasAnyAuthority('COMPANY', 'ADMIN')")
    public ResponseEntity<Map<String, String>> uploadLogo(@RequestParam("file") MultipartFile file,
                                                          HttpSession session) throws IOException {
        Long companyId = (Long) session.getAttribute("userId");
        String logo = companyService.updateLogo(companyId, file);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Logo updated successfully");
        response.put("logo", logo);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/change-password")
    @PreAuthorize("hasAnyAuthority('COMPANY', 'ADMIN')")
    public ResponseEntity<Map<String, String>> changePassword(@RequestBody Map<String, String> payload,
                                                              HttpSession session) {
        Long companyId = (Long) session.getAttribute("userId");
        companyService.changePassword(companyId, payload.get("oldPassword"), payload.get("newPassword"));
        Map<String, String> response = new HashMap<>();
        response.put("message", "Password changed successfully");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/update-email")
    @PreAuthorize("hasAnyAuthority('COMPANY', 'ADMIN')")
    public ResponseEntity<Map<String, String>> updateEmail(@RequestBody Map<String, String> payload,
                                                           HttpSession session) {
        Long companyId = (Long) session.getAttribute("userId");
        companyService.updateEmail(companyId, payload.get("newEmail"));
        Map<String, String> response = new HashMap<>();
        response.put("message", "Email updated successfully");
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete-account")
    @PreAuthorize("hasAnyAuthority('COMPANY', 'ADMIN')")
    public ResponseEntity<Map<String, String>> deleteAccount(HttpSession session) {
        Long companyId = (Long) session.getAttribute("userId");
        companyService.deleteAccount(companyId);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Account deleted successfully");
        return ResponseEntity.ok(response);
    }
}
