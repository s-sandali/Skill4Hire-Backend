package com.se.skill4hire.entity;


import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "company_profile")
public class CompanyProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String companyName;
    private String contactNumber;
    private String address;
    private String email;
    private String website;
    private String facebook;
    private String twitter;
    private String linkedin;

    private boolean notificationsEnabled;

    private String logoUrl;

    private String password; // hashed password
}