package com.se.skill4hire.service;

import com.se.skill4hire.model.CandidateCv;
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
    public CandidateCv upload(Long candidateId, MultipartFile file) throws IOException {
        CandidateCv entity = repository.findByCandidateId(candidateId).orElse(new CandidateCv());
        entity.setCandidateId(candidateId);
        entity.setFilename(file.getOriginalFilename() != null ? file.getOriginalFilename() : "cv");
        entity.setContentType(file.getContentType() != null ? file.getContentType() : "application/octet-stream");
        entity.setData(file.getBytes());
        return repository.save(entity);
    }

    @Transactional(readOnly = true)
    public CandidateCv getByCandidateId(Long candidateId) {
        CandidateCv cv = repository.findByCandidateId(candidateId)
                .orElseThrow(() -> new NotFoundException("CV not found for candidate " + candidateId));
        byte[] data = cv.getData();
        if (data != null) {
            int ignore = data.length;
        }
        return cv;
    }

    @Transactional
    public void deleteByCandidateId(Long candidateId) {
        if (!repository.existsByCandidateId(candidateId)) {
            throw new NotFoundException("CV not found for candidate " + candidateId);
        }
        repository.deleteByCandidateId(candidateId);
    }
}
