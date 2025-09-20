package com.se.skill4hire.dto.profile;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Holder for candidate profile DTOs.
 * Keeps request/response models together and avoids multiple public classes per file.
 */
public class CandidateProfileDTO {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Request {
        private String fullName;
        private String phone;
        private String location;
        private String education;
        private String experience;
        // Could also be a structured list (e.g., List<SkillDTO>) if needed
        private List<String> skills;
        private String summary;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Response {
        private Long id;
        private String fullName;
        // Keep email read-only; not editable via profile
        private String email;
        private String phone;
        private String location;
        private String education;
        private String experience;
        private String skills;
        private String summary;
    }
}