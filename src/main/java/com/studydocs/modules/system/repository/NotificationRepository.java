package com.studydocs.modules.system.repository;

import com.studydocs.modules.system.entity.NotificationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<NotificationEntity, String> {
    List<NotificationEntity> findByRecipientIdOrderByCreatedAtDesc(String recipientId);
}
