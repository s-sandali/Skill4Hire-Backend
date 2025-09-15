package com.se.skill4hire.service;

import com.se.skill4hire.model.CandidatePreferredCompany;
import com.se.skill4hire.model.CandidatePreferredJobRole;
import com.se.skill4hire.repository.CandidatePreferredCompanyRepository;
import com.se.skill4hire.repository.CandidatePreferredJobRoleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class CandidatePreferenceService {
    private final CandidatePreferredCompanyRepository companyRepo;
    private final CandidatePreferredJobRoleRepository roleRepo;

    public CandidatePreferenceService(CandidatePreferredCompanyRepository companyRepo,
                                      CandidatePreferredJobRoleRepository roleRepo) {
        this.companyRepo = companyRepo;
        this.roleRepo = roleRepo;
    }

    // Companies
    @Transactional(readOnly = true)
    public List<String> getPreferredCompanies(Long candidateId) {
        return companyRepo.findByCandidateId(candidateId)
                .stream().map(CandidatePreferredCompany::getCompanyName).toList();
    }

    @Transactional
    public void replacePreferredCompanies(Long candidateId, List<String> companies) {
        companyRepo.deleteByCandidateId(candidateId);
        addPreferredCompanies(candidateId, companies);
    }

    @Transactional
    public void addPreferredCompanies(Long candidateId, List<String> companies) {
        if (companies == null || companies.isEmpty()) return;
        Set<String> unique = new HashSet<>();
        for (String c : companies) {
            if (c == null) continue;
            String trimmed = c.trim();
            if (trimmed.isEmpty()) continue;
            String key = trimmed.toLowerCase();
            if (!unique.add(key)) continue;
            if (companyRepo.existsByCandidateIdAndCompanyNameIgnoreCase(candidateId, trimmed)) continue;
            CandidatePreferredCompany entity = new CandidatePreferredCompany();
            entity.setCandidateId(candidateId);
            entity.setCompanyName(trimmed);
            companyRepo.save(entity);
        }
    }

    @Transactional
    public void clearPreferredCompanies(Long candidateId) {
        companyRepo.deleteByCandidateId(candidateId);
    }

    // Job roles
    @Transactional(readOnly = true)
    public List<String> getPreferredJobRoles(Long candidateId) {
        return roleRepo.findByCandidateId(candidateId)
                .stream().map(CandidatePreferredJobRole::getJobRole).toList();
    }

    @Transactional
    public void replacePreferredJobRoles(Long candidateId, List<String> roles) {
        roleRepo.deleteByCandidateId(candidateId);
        addPreferredJobRoles(candidateId, roles);
    }

    @Transactional
    public void addPreferredJobRoles(Long candidateId, List<String> roles) {
        if (roles == null || roles.isEmpty()) return;
        Set<String> unique = new HashSet<>();
        for (String r : roles) {
            if (r == null) continue;
            String trimmed = r.trim();
            if (trimmed.isEmpty()) continue;
            String key = trimmed.toLowerCase();
            if (!unique.add(key)) continue;
            if (roleRepo.existsByCandidateIdAndJobRoleIgnoreCase(candidateId, trimmed)) continue;
            CandidatePreferredJobRole entity = new CandidatePreferredJobRole();
            entity.setCandidateId(candidateId);
            entity.setJobRole(trimmed);
            roleRepo.save(entity);
        }
    }

    @Transactional
    public void clearPreferredJobRoles(Long candidateId) {
        roleRepo.deleteByCandidateId(candidateId);
    }
}

