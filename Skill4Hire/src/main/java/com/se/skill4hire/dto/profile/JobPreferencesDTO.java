package com.se.skill4hire.dto.profile;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class JobPreferencesDTO {
    private String jobType;
    private String expectedSalary;
    private Boolean willingToRelocate = false;
}