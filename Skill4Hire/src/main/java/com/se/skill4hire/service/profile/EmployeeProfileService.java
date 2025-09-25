package com.se.skill4hire.service.profile;

import com.se.skill4hire.dto.profile.EmployeeProfileDTO;
import com.se.skill4hire.entity.EmployeeProfile;
import com.se.skill4hire.repository.EmployeeProfileRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import static org.springframework.http.HttpStatus.*;

@Service
@Transactional
public class EmployeeProfileService {

    private final EmployeeProfileRepository repository;

    public EmployeeProfileService(EmployeeProfileRepository repository) {
        this.repository = repository;
    }

    private EmployeeProfileDTO toDTO(EmployeeProfile e) {
        return new EmployeeProfileDTO(
                e.getId(), e.getName(), e.getEmail(), e.getPhone(), e.getPosition(), e.isActive()
        );
    }

    public EmployeeProfileDTO create(EmployeeProfileDTO dto) {
        if (repository.existsByEmail(dto.getEmail())) {
            // 409 Conflict for duplicate unique field
            throw new ResponseStatusException(CONFLICT, "Email already exists");
        }
        EmployeeProfile saved = repository.save(
                new EmployeeProfile(dto.getName(), dto.getEmail(), dto.getPhone(), dto.getPosition(), true)
        );
        return toDTO(saved);
    }

    @Transactional(readOnly = true)
    public EmployeeProfileDTO get(Long id) {
        EmployeeProfile e = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Employee profile not found"));
        return toDTO(e);
    }

    public EmployeeProfileDTO update(Long id, EmployeeProfileDTO dto) {
        EmployeeProfile e = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Employee profile not found"));

        // If changing email, ensure uniqueness
        if (!e.getEmail().equalsIgnoreCase(dto.getEmail()) && repository.existsByEmail(dto.getEmail())) {
            throw new ResponseStatusException(CONFLICT, "Email already exists");
        }

        e.setName(dto.getName());
        e.setEmail(dto.getEmail());
        e.setPhone(dto.getPhone());
        e.setPosition(dto.getPosition());
        // keep current active flag unless you want to allow editing via dto.setActive()

        return toDTO(repository.save(e));
    }

    public void deactivate(Long id) {
        EmployeeProfile e = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Employee profile not found"));
        e.setActive(false);           // soft delete (deactivate)
        repository.save(e);


    }
}
