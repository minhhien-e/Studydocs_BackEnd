package com.studydocs.modules.system.repository;

import com.studydocs.modules.system.entity.NotificationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<NotificationEntity, String> {
    List<NotificationEntity> findByRecipientIdAndIsTrashedFalseOrderByCreatedAtDesc(String recipientId);
    
    List<NotificationEntity> findByRecipientIdAndIsTrashedTrueOrderByCreatedAtDesc(String recipientId);
    
    @org.springframework.transaction.annotation.Transactional
    @org.springframework.data.jpa.repository.Modifying
    void deleteByRecipientIdAndTypeAndReferenceId(String recipientId, String type, String referenceId);
}
