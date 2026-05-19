package com.studydocs.review.infrastructure.persistence.repository;

import com.studydocs.review.infrastructure.persistence.entity.ReviewEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * Spring Data JPA Interface cho ReviewEntity.
 */
@Repository
public interface ReviewJpaRepository extends JpaRepository<ReviewEntity, UUID> {
    Page<ReviewEntity> findByDocumentIdAndParentIdIsNull(UUID documentId, Pageable pageable);
    Page<ReviewEntity> findByParentId(UUID parentId, Pageable pageable);
    
    Long countByDocumentId(UUID documentId);
    
    Optional<ReviewEntity> findByDocumentIdAndUserIdAndParentIdIsNull(UUID documentId, UUID userId);
    
    Page<ReviewEntity> findByUserId(UUID userId, Pageable pageable);
}
