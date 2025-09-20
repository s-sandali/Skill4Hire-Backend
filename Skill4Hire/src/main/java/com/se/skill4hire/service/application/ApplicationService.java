package com.se.skill4hire.service.application;

import com.se.skill4hire.entity.Application;
import com.se.skill4hire.repository.ApplicationRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ApplicationService {
    private final ApplicationRepository repository;

    public ApplicationService(ApplicationRepository repository) {
        this.repository = repository;
    }

    public List<CompanyView> getCompaniesByStatus(Long candidateId, Application.ApplicationStatus status) {
        return repository.findByCandidateIdAndStatus(candidateId, status)
                .stream()
                .map(a -> new CompanyView(a.getCompanyId(), a.getCompanyName(), a.getStatus().name(), a.getAppliedAt()))
                .collect(Collectors.toList());
    }

    public record CompanyView(Long companyId, String companyName, String status, java.time.LocalDateTime appliedAt) {}
}
