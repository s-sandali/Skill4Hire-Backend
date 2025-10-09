package com.se.skill4hire.service.profile;

import com.se.skill4hire.dto.profile.CompanyProfileDTO;
import com.se.skill4hire.entity.auth.Company;
import com.se.skill4hire.repository.auth.CompanyAuthRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;

@Service
public class CompanyProfileService {

    private final CompanyAuthRepository companyRepository;
    private final PasswordEncoder passwordEncoder;

    public CompanyProfileService(CompanyAuthRepository companyRepository,
                              PasswordEncoder passwordEncoder) {
        this.companyRepository = companyRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public CompanyProfileDTO getProfile(String companyId) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new RuntimeException("Company not found"));
        return mapToDTO(company);
    }

    public CompanyProfileDTO updateProfile(String companyId, CompanyProfileDTO dto) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new RuntimeException("Company not found"));

        company.setName(dto.getName());
        company.setDescription(dto.getDescription());
        company.setPhone(dto.getPhone());
        company.setWebsite(dto.getWebsite());
        company.setAddress(dto.getAddress());
        company.setFacebook(dto.getFacebook());
        company.setLinkedin(dto.getLinkedin());
        company.setTwitter(dto.getTwitter());

        companyRepository.save(company);
        return mapToDTO(company);
    }

    public String updateLogo(String companyId, MultipartFile file) throws IOException {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new RuntimeException("Company not found"));

        String base64Logo = Base64.getEncoder().encodeToString(file.getBytes());
        company.setLogo(base64Logo);

        companyRepository.save(company);
        return base64Logo;
    }
    public void changePassword(String companyId, String oldPassword, String newPassword) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new RuntimeException("Company not found"));
        if (!passwordEncoder.matches(oldPassword, company.getPassword())) {
            throw new RuntimeException("Old password is incorrect");
        }
        company.setPassword(passwordEncoder.encode(newPassword));
        companyRepository.save(company);
    }

    // Update email
    public void updateEmail(String companyId, String newEmail) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new RuntimeException("Company not found"));
        company.setEmail(newEmail);
        companyRepository.save(company);
    }

    // Delete account
    public void deleteAccount(String companyId) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new RuntimeException("Company not found"));
        companyRepository.delete(company);
    }

    private CompanyProfileDTO mapToDTO(Company company) {
        CompanyProfileDTO dto = new CompanyProfileDTO();
        dto.setName(company.getName());
        dto.setDescription(company.getDescription());
        dto.setPhone(company.getPhone());
        dto.setWebsite(company.getWebsite());
        dto.setAddress(company.getAddress());
        dto.setFacebook(company.getFacebook());
        dto.setLinkedin(company.getLinkedin());
        dto.setTwitter(company.getTwitter());
        dto.setLogo(company.getLogo());
        return dto;
    }
}
