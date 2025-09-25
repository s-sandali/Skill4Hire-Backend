package com.se.skill4hire.controller.profile;

import com.se.skill4hire.dto.profile.EmployeeProfileDTO;
import com.se.skill4hire.service.profile.EmployeeProfileService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/api/employees")
public class EmployeeProfileController {

    private final EmployeeProfileService service;

    public EmployeeProfileController(EmployeeProfileService service) {
        this.service = service;
    }

    // Create profile
    // POST /api/employees/profile
    @PostMapping("/profile")
    public ResponseEntity<EmployeeProfileDTO> createProfile(@Valid @RequestBody EmployeeProfileDTO dto,
                                                            UriComponentsBuilder uriBuilder) {
        EmployeeProfileDTO created = service.create(dto);
        return ResponseEntity
                .created(uriBuilder.path("/api/employees/profile/{id}").build(created.getId()))
                .body(created); // 201 Created
    }

    // Get profile by id
    // GET /api/employees/profile/{id}
    @GetMapping("/profile/{id}")
    public ResponseEntity<EmployeeProfileDTO> getProfile(@PathVariable Long id) {
        return ResponseEntity.ok(service.get(id)); // 200 OK
    }

    // Update profile
    // PUT /api/employees/profile/{id}
    @PutMapping("/profile/{id}")
    public ResponseEntity<EmployeeProfileDTO> updateProfile(@PathVariable Long id,
                                                            @Valid @RequestBody EmployeeProfileDTO dto) {
        return ResponseEntity.ok(service.update(id, dto)); // 200 OK
    }

    // Deactivate (soft delete)
    // DELETE /api/employees/profile/{id}
    @DeleteMapping("/profile/{id}")
    public ResponseEntity<Void> deleteOrDeactivate(@PathVariable Long id) {
        service.deactivate(id);
        return ResponseEntity.ok().build(); // 200 OK (or 204 No Content)
    }
}
