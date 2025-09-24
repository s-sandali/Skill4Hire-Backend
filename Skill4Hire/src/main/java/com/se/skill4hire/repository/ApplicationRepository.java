package com.se.skill4hire.repository;

import com.se.skill4hire.entity.Application;
import com.se.skill4hire.entity.Application.SubmissionSource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, Long> {
    List<Application> findByCandidateId(Long candidateId);
    List<Application> findByCandidateIdAndSubmittedBy(Long candidateId, SubmissionSource submittedBy);
}
