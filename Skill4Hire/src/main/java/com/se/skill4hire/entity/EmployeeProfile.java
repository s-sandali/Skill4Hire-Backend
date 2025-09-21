package com.se.skill4hire.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(
        name = "employees",
        uniqueConstraints = @UniqueConstraint(columnNames = "email")
)
public class EmployeeProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank @Size(max = 100)
    @Column(nullable = false)
    private String name;

    @NotBlank @Email @Size(max = 150)
    @Column(nullable = false, unique = true)
    private String email;

    @Size(max = 20)
    private String phone;

    @Size(max = 100)
    private String position;

    @Column(nullable = false)
    private boolean active = true; // soft-delete flag

    // getters/setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

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
