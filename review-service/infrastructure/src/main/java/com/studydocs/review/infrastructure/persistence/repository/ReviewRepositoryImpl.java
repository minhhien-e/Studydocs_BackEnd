package com.studydocs.review.infrastructure.persistence.repository;

import com.studydocs.review.domain.model.ReviewModel;
import com.studydocs.review.domain.repository.ReviewRepository;
import com.studydocs.review.infrastructure.persistence.PersistenceMapper;
import com.studydocs.review.infrastructure.persistence.entity.ReviewEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

/**
 * Triển khai ReviewRepository (Domain) sử dụng Spring Data JPA.
 */
@Component
@RequiredArgsConstructor
public class ReviewRepositoryImpl implements ReviewRepository {

    private final ReviewJpaRepository jpaRepository;
    private final PersistenceMapper mapper;

    @Override
    public ReviewModel save(ReviewModel review) {
        ReviewEntity entity = mapper.toEntity(review);
        ReviewEntity saved = jpaRepository.save(entity);
        return mapper.toDomain(saved);
    }

    @Override
    public Optional<ReviewModel> findById(UUID id) {
        return jpaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public Page<ReviewModel> findByDocumentIdAndParentIsNull(UUID documentId, Pageable pageable) {
        return jpaRepository.findByDocumentIdAndParentIdIsNull(documentId, pageable)
                .map(mapper::toDomain);
    }

    @Override
    public void delete(ReviewModel review) {
        jpaRepository.delete(mapper.toEntity(review));
    }

    @Override
    public Page<ReviewModel> findByParentId(UUID parentId, Pageable pageable) {
        return jpaRepository.findByParentId(parentId, pageable).map(mapper::toDomain);
    }



    @Override
    public Long countByDocumentId(UUID documentId) {
        return jpaRepository.countByDocumentId(documentId);
    }

    @Override
    public Optional<ReviewModel> findByDocumentIdAndUserIdAndParentIsNull(UUID documentId, UUID userId) {
        return jpaRepository.findByDocumentIdAndUserIdAndParentIdIsNull(documentId, userId).map(mapper::toDomain);
    }

    @Override
    public Page<ReviewModel> findByUserId(UUID userId, Pageable pageable) {
        return jpaRepository.findByUserId(userId, pageable).map(mapper::toDomain);
    }
}
