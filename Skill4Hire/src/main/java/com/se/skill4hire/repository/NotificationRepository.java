package com.se.skill4hire.repository;

import com.se.skill4hire.entity.AppNotification;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends MongoRepository<AppNotification, String> {
    List<AppNotification> findByUserIdOrderByCreatedAtDesc(String userId);
}

