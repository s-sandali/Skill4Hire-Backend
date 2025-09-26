package com.se.skill4hire.dto.profile;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ExperienceDTO {
    private Boolean isExperienced = false;
    private String role;
    private String company;
    private Integer yearsOfExperience;
}