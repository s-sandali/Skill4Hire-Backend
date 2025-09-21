package com.se.skill4hire.service.profile;

import com.se.skill4hire.dto.profile.EmployeeProfileDTO;
import com.se.skill4hire.entity.EmployeeProfile;
import com.se.skill4hire.repository.EmployeeProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class EmployeeProfileService {

    @Autowired
    private EmployeeProfileRepository EmployeeProfileRepository ;

    // Create Employee Profile
    public EmployeeProfileDTO createEmployeeProfile(EmployeeProfileDTO employeeProfileDTO) {
        // Check if email already exists
        if (EmployeeProfileRepository .existsByEmail(employeeProfileDTO.getEmail())) {
            throw new RuntimeException("Email already exists: " + employeeProfileDTO.getEmail());
        }

        // Convert DTO to Entity
        EmployeeProfile employeeProfile = new EmployeeProfile();
        employeeProfile.setName(employeeProfileDTO.getUsername());
        employeeProfile.setEmail(employeeProfileDTO.getEmail());
        // Set default values for phone and position since they're not in DTO
        employeeProfile.setPhone("");
        employeeProfile.setPosition("");
        employeeProfile.setActive(true);

        // Save the entity
        EmployeeProfile savedProfile = EmployeeProfileRepository .save(employeeProfile);

        // Convert back to DTO and return
        EmployeeProfileDTO responseDTO = new EmployeeProfileDTO();
        responseDTO.setUsername(savedProfile.getName());
        responseDTO.setEmail(savedProfile.getEmail());
        // Password is not stored in entity, so we don't set it in response

        return responseDTO;
    }

    // Get Employee Profile by ID
    public EmployeeProfileDTO getEmployeeProfileById(Long id) {
        Optional<EmployeeProfile> employeeProfile = EmployeeProfileRepository .findById(id);
        if (employeeProfile.isPresent()) {
            EmployeeProfile profile = employeeProfile.get();
            EmployeeProfileDTO dto = new EmployeeProfileDTO();
            dto.setUsername(profile.getName());
            dto.setEmail(profile.getEmail());
            // Password is not stored in entity
            return dto;
        }
        throw new RuntimeException("Employee profile not found with id: " + id);
    }

    // Get all Employee Profiles
    public List<EmployeeProfile> getAllEmployeeProfiles() {
        return EmployeeProfileRepository .findAll();
    }

    // Get all active Employee Profiles
    public List<EmployeeProfile> getAllActiveEmployeeProfiles() {
        return EmployeeProfileRepository .findByActiveTrue();
    }

    // Update Employee Profile
    public EmployeeProfileDTO updateEmployeeProfile(Long id, EmployeeProfileDTO employeeProfileDTO) {
        Optional<EmployeeProfile> existingProfile = EmployeeProfileRepository .findById(id);
        if (existingProfile.isPresent()) {
            EmployeeProfile profile = existingProfile.get();

            // Check if email is being changed and if it already exists for another employee
            if (!profile.getEmail().equals(employeeProfileDTO.getEmail()) &&
                    EmployeeProfileRepository .existsByEmail(employeeProfileDTO.getEmail())) {
                throw new RuntimeException("Email already exists: " + employeeProfileDTO.getEmail());
            }

            // Update the profile
            profile.setName(employeeProfileDTO.getUsername());
            profile.setEmail(employeeProfileDTO.getEmail());
            profile.setUpdatedAt(LocalDateTime.now());

            EmployeeProfile updatedProfile = EmployeeProfileRepository .save(profile);

            // Convert to DTO for response
            EmployeeProfileDTO responseDTO = new EmployeeProfileDTO();
            responseDTO.setUsername(updatedProfile.getName());
            responseDTO.setEmail(updatedProfile.getEmail());

            return responseDTO;
        }
        throw new RuntimeException("Employee profile not found with id: " + id);
    }

    // Deactivate Employee Profile (soft delete)
    public void deactivateEmployeeProfile(Long id) {
        Optional<EmployeeProfile> employeeProfile = EmployeeProfileRepository .findById(id);
        if (employeeProfile.isPresent()) {
            EmployeeProfile profile = employeeProfile.get();
            profile.setActive(false);
            profile.setUpdatedAt(LocalDateTime.now());
            EmployeeProfileRepository .save(profile);
        } else {
            throw new RuntimeException("Employee profile not found with id: " + id);
        }
    }

    // Check if email exists
    public boolean emailExists(String email) {
        return EmployeeProfileRepository .existsByEmail(email);
    }

    // Get Employee Profile by email
    public EmployeeProfileDTO getEmployeeProfileByEmail(String email) {
        Optional<EmployeeProfile> employeeProfile = EmployeeProfileRepository .findByEmail(email);
        if (employeeProfile.isPresent()) {
            EmployeeProfile profile = employeeProfile.get();
            EmployeeProfileDTO dto = new EmployeeProfileDTO();
            dto.setUsername(profile.getName());
            dto.setEmail(profile.getEmail());
            return dto;
        }
        throw new RuntimeException("Employee profile not found with email: " + email);
    }

    // Update phone number
    public void updateEmployeePhone(Long id, String phone) {
        Optional<EmployeeProfile> employeeProfile = EmployeeProfileRepository .findById(id);
        if (employeeProfile.isPresent()) {
            EmployeeProfile profile = employeeProfile.get();
            profile.setPhone(phone);
            profile.setUpdatedAt(LocalDateTime.now());
            EmployeeProfileRepository .save(profile);
        } else {
            throw new RuntimeException("Employee profile not found with id: " + id);
        }
    }

    // Update position
    public void updateEmployeePosition(Long id, String position) {
        Optional<EmployeeProfile> employeeProfile = EmployeeProfileRepository.findById(id);
        if (employeeProfile.isPresent()) {
            EmployeeProfile profile = employeeProfile.get();
            profile.setPosition(position);
            profile.setUpdatedAt(LocalDateTime.now());
            EmployeeProfileRepository.save(profile);
        } else {
            throw new RuntimeException("Employee profile not found with id: " + id);
        }
    }

    // Get full employee profile (including phone and position)
    public EmployeeProfile getFullEmployeeProfile(Long id) {
        Optional<EmployeeProfile> employeeProfile = EmployeeProfileRepository.findById(id);
        if (employeeProfile.isPresent()) {
            return employeeProfile.get();
        }
        throw new RuntimeException("Employee profile not found with id: " + id);
    }
}