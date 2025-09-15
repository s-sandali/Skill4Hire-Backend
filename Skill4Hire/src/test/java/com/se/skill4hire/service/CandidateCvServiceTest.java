package com.se.skill4hire.service;

import com.se.skill4hire.model.CandidateCv;
import com.se.skill4hire.repository.CandidateCvRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;

@SpringBootTest
class CandidateCvServiceTest {

    @Autowired
    private CandidateCvService service;

    @Autowired
    private CandidateCvRepository repository;

    @Test
    @Transactional
    void upload_get_delete_flow_works() throws Exception {
        Long candidateId = 123L;
        byte[] content = "Hello CV".getBytes(StandardCharsets.UTF_8);

        MockMultipartFile file = new MockMultipartFile(
                "file",
                "cv.txt",
                "text/plain",
                content
        );

        CandidateCv saved = service.upload(candidateId, file);
        Assertions.assertNotNull(saved.getId());
        Assertions.assertEquals(candidateId, saved.getCandidateId());
        Assertions.assertEquals("cv.txt", saved.getFilename());

        CandidateCv fetched = service.getByCandidateId(candidateId);
        Assertions.assertEquals("text/plain", fetched.getContentType());
        Assertions.assertArrayEquals(content, fetched.getData());

        service.deleteByCandidateId(candidateId);
        Assertions.assertFalse(repository.existsByCandidateId(candidateId));
    }
}

