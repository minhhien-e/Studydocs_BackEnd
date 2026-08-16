package com.studydocs.modules.review.repository;

import com.studydocs.modules.review.entity.DocumentReviewEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<DocumentReviewEntity, String> {
    List<DocumentReviewEntity> findByDocumentId(String documentId);
}
