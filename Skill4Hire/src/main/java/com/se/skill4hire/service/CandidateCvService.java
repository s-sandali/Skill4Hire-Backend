package com.se.skill4hire.service;


import com.se.skill4hire.model.CandidateCv;
import com.se.skill4hire.repository.CandidateCvRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;


@Service
public class CandidateCvService {
    private final CandidateCvRepository repository;


    public CandidateCvService(CandidateCvRepository repository) {
        this.repository = repository;
    }


    public CandidateCv upload(Long candidateId, MultipartFile file) throws IOException {
        CandidateCv entity = repository.findByCandidateId(candidateId).orElse(new CandidateCv());
        entity.setCandidateId(candidateId);
        entity.setFilename(file.getOriginalFilename() != null ? file.getOriginalFilename() : "cv");
        entity.setContentType(file.getContentType() != null ? file.getContentType() : "application/octet-stream");
        entity.setData(file.getBytes());
        return repository.save(entity);
    }


    public CandidateCv getByCandidateId(Long candidateId) {
        return repository.findByCandidateId(candidateId)
                .orElseThrow(() -> new RuntimeException("CV not found for candidate " + candidateId));
    }


    public void deleteByCandidateId(Long candidateId) {
        if (!repository.existsByCandidateId(candidateId)) {
            throw new RuntimeException("CV not found for candidate " + candidateId);
        }
        repository.deleteByCandidateId(candidateId);
    }
}