package com.studydocs.review.domain.repository;

import com.studydocs.review.domain.model.ReviewModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

/**
 * Interface Repository cho Review.
 */
public interface ReviewRepository {
    ReviewModel save(ReviewModel review);
    Optional<ReviewModel> findById(UUID id);
    Page<ReviewModel> findByDocumentIdAndParentIsNull(UUID documentId, Pageable pageable);
    void delete(ReviewModel review);
    Page<ReviewModel> findByParentId(UUID parentId, Pageable pageable);
    Long countByDocumentId(UUID documentId);
    Optional<ReviewModel> findByDocumentIdAndUserIdAndParentIsNull(UUID documentId, UUID userId);
    Page<ReviewModel> findByUserId(UUID userId, Pageable pageable);
}
