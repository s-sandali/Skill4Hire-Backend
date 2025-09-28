package com.se.skill4hire.dto.profile;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProfileCompletenessDTO {
    private double completenessPercentage; // CHANGED FROM 'completeness'
    private String message;
}