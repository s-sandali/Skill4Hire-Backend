package com.se.skill4hire.dto.job;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JobSearchRequestDTO {
    private String keyword;
    private String type;
    private String location;
    private Double minSalary;
    private Integer maxExperience;

    // Helper methods to check if filters are applied
    public boolean hasKeyword() {
        return keyword != null && !keyword.trim().isEmpty();
    }

    public boolean hasFilters() {
        return hasKeyword() || type != null || location != null || minSalary != null || maxExperience != null;
    }
}