package com.se.skill4hire.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(
        name = "employee_profiles",
        uniqueConstraints = @UniqueConstraint(name = "uk_employeeprofile_email", columnNames = "email")
)
public class EmployeeProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name is required")
    @Column(nullable = false)
    private String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Email is invalid")
    @Column(nullable = false, unique = true, length = 180)
    private String email;

    @NotBlank(message = "Phone is required")
    @Column(nullable = false, length = 30)
    private String phone;

    @NotBlank(message = "Position is required")
    @Column(nullable = false, length = 120)
    private String position;

    @Column(nullable = false)
    private boolean active = true;

    public EmployeeProfile() {}

    public EmployeeProfile(String name, String email, String phone, String position, boolean active) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.position = position;
        this.active = active;
    }

    // Getters & Setters
    public Long getId() { return id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getPosition() { return position; }
    public void setPosition(String position) { this.position = position; }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
}
