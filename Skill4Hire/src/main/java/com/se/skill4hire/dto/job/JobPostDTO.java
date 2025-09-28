package com.se.skill4hire.dto.job;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class JobPostDTO {
    
    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "Description is required")
    private String description;

    @NotBlank(message = "Job type is required")
    private String type; // FULL_TIME, PART_TIME, CONTRACT, etc.
    
    @NotBlank(message = "Location is required")
    private String location;
    
    private Double salary;
    private Integer experience; // in years
    
    @NotNull(message = "Application deadline is required")
    private LocalDate deadline;

    // No company field - it's set from session in the controller
}