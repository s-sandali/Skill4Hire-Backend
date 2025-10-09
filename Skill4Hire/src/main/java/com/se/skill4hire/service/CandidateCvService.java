package com.se.skill4hire.service;

import com.se.skill4hire.entity.candidate.CandidateCv;
import com.se.skill4hire.exception.NotFoundException;
import com.se.skill4hire.repository.CandidateCvRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class CandidateCvService {
    private final CandidateCvRepository repository;

    public CandidateCvService(CandidateCvRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public CandidateCv upload(String candidateId, MultipartFile file) throws IOException {
        if (candidateId == null) {
            throw new IllegalArgumentException("candidateId must not be null");
        }
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("file must not be null or empty");
        }

        CandidateCv entity = repository.findByCandidateId(candidateId).orElse(new CandidateCv());
        entity.setCandidateId(candidateId);
        entity.setFilename(file.getOriginalFilename() != null ? file.getOriginalFilename() : "cv");
        entity.setContentType(file.getContentType() != null ? file.getContentType() : "application/octet-stream");
        entity.setData(file.getBytes());
        return repository.save(entity);
    }

    @Transactional(readOnly = true)
    public CandidateCv getByCandidateId(String candidateId) {
        if (candidateId == null) {
            throw new IllegalArgumentException("candidateId must not be null");
        }

        CandidateCv cv = repository.findByCandidateId(candidateId)
                .orElseThrow(() -> new NotFoundException("CV not found for candidate " + candidateId));

        // Access LOB inside transaction so data is initialized before returning
        cv.getData();
        return cv;
    }

    @Transactional
    public void deleteByCandidateId(String candidateId) {
        if (candidateId == null) {
            throw new IllegalArgumentException("candidateId must not be null");
        }
        if (!repository.existsByCandidateId(candidateId)) {
            throw new NotFoundException("CV not found for candidate " + candidateId);
        }
        repository.deleteByCandidateId(candidateId);
    }
}
