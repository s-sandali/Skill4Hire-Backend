package com.se.skill4hire.controller;

import com.se.skill4hire.entity.auth.User;
import com.se.skill4hire.service.CandidateCvService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/candidates/{candidateId}/cv")
public class CandidateCvController {

    private final CandidateCvService service;

    public CandidateCvController(CandidateCvService service) {
        this.service = service;
    }

    /**
     * Upload or replace a candidate's CV.
     * cURL:
     *   curl -X POST -F file=@/path/to/cv.pdf http://localhost:8080/api/candidates/123/cv
     */
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<CandidateCvResponse> upload(@PathVariable Long candidateId,
                                                      @RequestPart("file") MultipartFile file) throws Exception {
        User.CandidateCv saved = service.upload(candidateId, file);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(CandidateCvResponse.from(saved));
    }

    /**
     * Update/replace also allowed via PUT (idempotent).
     */
    @PutMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<CandidateCvResponse> replace(@PathVariable Long candidateId,
                                                       @RequestPart("file") MultipartFile file) throws Exception {
        User.CandidateCv saved = service.upload(candidateId, file);
        return ResponseEntity.ok(CandidateCvResponse.from(saved));
    }

    /** Download the CV binary. */
    @GetMapping
    public ResponseEntity<byte[]> download(@PathVariable Long candidateId) {
        User.CandidateCv cv = service.getByCandidateId(candidateId);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + cv.getFilename() + "\"")
                .contentType(MediaType.parseMediaType(cv.getContentType()))
                .body(cv.getData());
    }

    /** Delete the CV. */
    @DeleteMapping
    public ResponseEntity<Void> delete(@PathVariable Long candidateId) {
        service.deleteByCandidateId(candidateId);
        return ResponseEntity.noContent().build();
    }

    // Lightweight DTO to avoid exposing byte[] in JSON responses
    public record CandidateCvResponse(Long id, Long candidateId, String filename, String contentType) {
        public static CandidateCvResponse from(User.CandidateCv cv) {
            return new CandidateCvResponse(cv.getId(), cv.getCandidateId(), cv.getFilename(), cv.getContentType());
        }
    }
}
