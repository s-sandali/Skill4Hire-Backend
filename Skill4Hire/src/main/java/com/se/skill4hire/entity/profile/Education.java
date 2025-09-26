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
public class Education {
    @Size(max = 100, message = "Degree must be less than 100 characters")
    private String degree;

    @Size(max = 100, message = "Institution must be less than 100 characters")
    private String institution;

    private Integer graduationYear;
}