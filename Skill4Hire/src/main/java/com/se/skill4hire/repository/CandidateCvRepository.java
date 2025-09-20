package com.se.skill4hire.repository;


import com.se.skill4hire.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.Optional;


public interface CandidateCvRepository extends JpaRepository<User.CandidateCv, Long> {
    Optional<User.CandidateCv> findByCandidateId(Long candidateId);
    boolean existsByCandidateId(Long candidateId);
    void deleteByCandidateId(Long candidateId);
}