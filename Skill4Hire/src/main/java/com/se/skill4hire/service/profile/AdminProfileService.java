package com.se.skill4hire.service.profile;

import com.se.skill4hire.dto.profile.AdminProfileDTO;
import com.se.skill4hire.entity.AdminProfile;
import com.se.skill4hire.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AdminProfileService {

    @Autowired
    private AdminRepository adminRepository;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    // Register admin
    public AdminProfile registerAdmin(AdminProfileDTO dto) {
        AdminProfile admin = new AdminProfile();
        admin.setUsername(dto.getUsername());
        admin.setEmail(dto.getEmail());
        admin.setPassword(passwordEncoder.encode(dto.getPassword()));
        return adminRepository.save(admin);
    }

    // Login admin
    public boolean loginAdmin(AdminProfileDTO dto) {
        Optional<AdminProfile> adminOpt = adminRepository.findByEmail(dto.getEmail());
        if (adminOpt.isPresent()) {
            return passwordEncoder.matches(dto.getPassword(), adminOpt.get().getPassword());
        }
        return false;
    }
}
