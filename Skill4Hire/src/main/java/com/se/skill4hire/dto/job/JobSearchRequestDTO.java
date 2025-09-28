package com.se.skill4hire.dto.job;

public class JobSearchRequestDTO {
    private String keyword;
    private String type;
    private String location;
    private Double minSalary;
    private Integer maxExperience;

    public JobSearchRequestDTO() {
        // Default constructor
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Double getMinSalary() {
        return minSalary;
    }

    public void setMinSalary(Double minSalary) {
        this.minSalary = minSalary;
    }

    public Integer getMaxExperience() {
        return maxExperience;
    }

    public void setMaxExperience(Integer maxExperience) {
        this.maxExperience = maxExperience;
    }

    public boolean hasKeyword() {
        return keyword != null && !keyword.trim().isEmpty();
    }

    public boolean hasFilters() {
        return hasKeyword() || type != null || location != null || minSalary != null || maxExperience != null;
    }
}
