package com.se.skill4hire.entity.auth;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "companies")
@NoArgsConstructor
@Getter
@Setter
public class Company extends User {

    private String name;
    private String description;
    private String phone;
    private String website;
    private String address;
    private String facebook;
    private String linkedin;
    private String twitter;
    private String logo;

    public Company(String email, String password, String name, String description,
                   String phone, String website, String address,
                   String facebook, String linkedin, String twitter) {
        super(email, password);
        this.name = name;
        this.description = description;
        this.phone = phone;
        this.website = website;
        this.address = address;
        this.facebook = facebook;
        this.linkedin = linkedin;
        this.twitter = twitter;
    }

    @Override
    public void setRole(String role) {
        super.setRole(role);
    }
}
