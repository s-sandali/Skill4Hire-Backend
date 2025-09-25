package com.se.skill4hire.dto.profile;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/**
 * Single DTO used for both requests and responses.
 * For creates/updates, 'id' can be null/ignored by clients.
 */
public class EmployeeProfileDTO {
    private Long id;

    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Email is invalid")
    private String email;

    @NotBlank(message = "Phone is required")
    private String phone;

    @NotBlank(message = "Position is required")
    private String position;

    private boolean active = true;

    public EmployeeProfileDTO() {}

    public EmployeeProfileDTO(Long id, String name, String email, String phone, String position, boolean active) {
        this.id = id; this.name = name; this.email = email; this.phone = phone; this.position = position; this.active = active;
    }

    // Getters & Setters
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
