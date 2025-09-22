// JobPreferences.java
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
public class JobPreferences {
    @Size(max = 50, message = "Job type must be less than 50 characters")
    private String jobType;

    @Size(max = 100, message = "Expected salary must be less than 100 characters")
    private String expectedSalary;

    private Boolean willingToRelocate = false;
}