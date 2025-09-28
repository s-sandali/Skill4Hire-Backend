package com.se.skill4hire.entity.profile;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.Size;

@Embeddable
public class Education {
    @Size(max = 100, message = "Degree must be less than 100 characters")
    private String degree;

    @Size(max = 100, message = "Institution must be less than 100 characters")
    private String institution;

    private Integer graduationYear;

    public Education() {
        // Default constructor
    }

    public Education(String degree, String institution, Integer graduationYear) {
        this.degree = degree;
        this.institution = institution;
        this.graduationYear = graduationYear;
    }

    public String getDegree() {
        return degree;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }

    public String getInstitution() {
        return institution;
    }

    public void setInstitution(String institution) {
        this.institution = institution;
    }

    public Integer getGraduationYear() {
        return graduationYear;
    }

    public void setGraduationYear(Integer graduationYear) {
        this.graduationYear = graduationYear;
    }
}
