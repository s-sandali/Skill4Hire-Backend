package com.se.skill4hire.entity.profile;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Experience {
    private Boolean isExperienced = false;

    @Size(max = 100, message = "Role must be less than 100 characters")
    private String role;

    @Size(max = 100, message = "Company must be less than 100 characters")
    private String company;

    private Integer yearsOfExperience;
}