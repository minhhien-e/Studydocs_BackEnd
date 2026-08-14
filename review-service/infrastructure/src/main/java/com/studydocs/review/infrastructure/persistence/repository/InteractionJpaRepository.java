package com.studydocs.review.infrastructure.persistence.repository;

import com.studydocs.review.domain.enums.InteractionType;
import com.studydocs.review.domain.enums.TargetType;
import com.studydocs.review.infrastructure.persistence.entity.InteractionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * Spring Data JPA Interface cho InteractionEntity.
 */
@Repository
public interface InteractionJpaRepository extends JpaRepository<InteractionEntity, UUID> {
    Optional<InteractionEntity> findByUserIdAndTargetIdAndTargetType(UUID userId, UUID targetId, TargetType targetType);
    long countByTargetIdAndTargetTypeAndInteractionType(UUID targetId, TargetType targetType, InteractionType interactionType);
}
