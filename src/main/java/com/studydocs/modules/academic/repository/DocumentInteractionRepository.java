package com.studydocs.modules.academic.repository;

import com.studydocs.modules.academic.entity.DocumentInteractionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DocumentInteractionRepository extends JpaRepository<DocumentInteractionEntity, String> {
    Optional<DocumentInteractionEntity> findByDocumentIdAndUserIdAndType(String documentId, String userId, String type);
    boolean existsByDocumentIdAndUserIdAndType(String documentId, String userId, String type);
}
