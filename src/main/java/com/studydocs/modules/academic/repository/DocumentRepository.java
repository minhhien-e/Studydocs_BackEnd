package com.studydocs.modules.academic.repository;

import com.studydocs.modules.academic.entity.DocumentEntity;
import com.studydocs.modules.academic.entity.DocumentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DocumentRepository extends JpaRepository<DocumentEntity, String> {
    List<DocumentEntity> findByUploaderId(String uploaderId);
    List<DocumentEntity> findTop10ByIsPublicTrueAndStatusOrderByLikeCountDesc(DocumentStatus status);
    List<DocumentEntity> findTop10ByIsPublicTrueAndStatusOrderByCreatedAtDesc(DocumentStatus status);

    @Query("SELECT d FROM DocumentEntity d JOIN DocumentInteractionEntity i ON d.id = i.documentId WHERE i.userId = :userId AND i.type = 'BOOKMARK'")
    List<DocumentEntity> findBookmarkedByUserId(@Param("userId") String userId);

    @Query("SELECT d FROM DocumentEntity d WHERE d.isPublic = true AND d.status = com.studydocs.modules.academic.entity.DocumentStatus.COMPLETED AND (:q IS NULL OR LOWER(d.title) LIKE LOWER(CONCAT('%', :q, '%')) OR LOWER(d.description) LIKE LOWER(CONCAT('%', :q, '%')))")
    List<DocumentEntity> searchDocuments(@Param("q") String query);
}
