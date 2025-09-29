package com.se.skill4hire.dto.profile;

public class EducationDTO {
    private String degree;
    private String institution;
    private Integer graduationYear;

    public EducationDTO() {
        // Default constructor
    }

    public EducationDTO(String degree, String institution, Integer graduationYear) {
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
