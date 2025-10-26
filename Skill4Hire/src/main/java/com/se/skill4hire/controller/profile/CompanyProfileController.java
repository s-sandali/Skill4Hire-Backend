package com.se.skill4hire.controller.profile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.se.skill4hire.dto.profile.CompanyProfileDTO;
import com.se.skill4hire.service.profile.CompanyProfileService;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/companies") // updated path
public class CompanyProfileController {

    private final CompanyProfileService companyService; // consistent service naming

    public CompanyProfileController(CompanyProfileService companyService) {
        this.companyService = companyService;
    }

    @GetMapping("/profile")
    @PreAuthorize("hasAuthority('COMPANY')")
    public ResponseEntity<CompanyProfileDTO> getProfile(HttpSession session) {
        String companyId = (String) session.getAttribute("userId");
        CompanyProfileDTO profile = companyService.getProfile(companyId);
        return ResponseEntity.ok(profile);
    }

    @PutMapping("/profile")
    @PreAuthorize("hasAuthority('COMPANY')")
    public ResponseEntity<CompanyProfileDTO> updateProfile(@RequestBody CompanyProfileDTO dto,
                                                           HttpSession session) {
        String companyId = (String) session.getAttribute("userId");
        CompanyProfileDTO updatedProfile = companyService.updateProfile(companyId, dto);
        return ResponseEntity.ok(updatedProfile);
    }

    @PostMapping("/upload/logo")
    @PreAuthorize("hasAuthority('COMPANY')")
    public ResponseEntity<Map<String, String>> uploadLogo(@RequestParam("file") MultipartFile file,
                                                          HttpSession session) throws IOException {
        String companyId = (String) session.getAttribute("userId");
        if (file == null || file.isEmpty()) {
            Map<String, String> error = new HashMap<>();
            error.put("message", "Logo file is required");
            return ResponseEntity.badRequest().body(error);
        }
        String logo = companyService.updateLogo(companyId, file);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Logo updated successfully");
        response.put("logo", logo);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/logo")
    @PreAuthorize("hasAuthority('COMPANY')")
    public ResponseEntity<Map<String, String>> removeLogo(HttpSession session) {
        String companyId = (String) session.getAttribute("userId");
        companyService.removeLogo(companyId);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Logo removed successfully");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/change-password")
    @PreAuthorize("hasAuthority('COMPANY')")
    public ResponseEntity<Map<String, String>> changePassword(@RequestBody Map<String, String> payload,
                                                              HttpSession session) {
        String companyId = (String) session.getAttribute("userId");
        companyService.changePassword(companyId, payload.get("oldPassword"), payload.get("newPassword"));
        Map<String, String> response = new HashMap<>();
        response.put("message", "Password changed successfully");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/update-email")
    @PreAuthorize("hasAuthority('COMPANY')")
    public ResponseEntity<Map<String, String>> updateEmail(@RequestBody Map<String, String> payload,
                                                           HttpSession session) {
        String companyId = (String) session.getAttribute("userId");
        companyService.updateEmail(companyId, payload.get("newEmail"));
        Map<String, String> response = new HashMap<>();
        response.put("message", "Email updated successfully");
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete-account")
    @PreAuthorize("hasAuthority('COMPANY')")
    public ResponseEntity<Map<String, String>> deleteAccount(HttpSession session) {
        String companyId = (String) session.getAttribute("userId");
        companyService.deleteAccount(companyId);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Account deleted successfully");
        return ResponseEntity.ok(response);
    }
}
