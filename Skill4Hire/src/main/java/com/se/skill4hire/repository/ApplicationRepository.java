package com.se.skill4hire.repository;

import com.se.skill4hire.entity.Application;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ApplicationRepository extends JpaRepository<Application, Long> {

    List<Application> findByCandidateId(Long candidateId);

    List<Application> findByCandidateIdAndStatus(Long candidateId, Application.ApplicationStatus status);

    @Query("select distinct a.companyId from Application a where a.candidateId = :candidateId and a.status = :status")
    List<Long> findDistinctCompanyIdsByCandidateAndStatus(@Param("candidateId") Long candidateId,
                                                          @Param("status") Application.ApplicationStatus status);

    @Query("select distinct a.companyName from Application a where a.candidateId = :candidateId and a.status = :status and a.companyName is not null")
    List<String> findDistinctCompanyNamesByCandidateAndStatus(@Param("candidateId") Long candidateId,
                                                              @Param("status") Application.ApplicationStatus status);
}
