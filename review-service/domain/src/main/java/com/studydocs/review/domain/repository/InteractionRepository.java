package com.studydocs.review.domain.repository;

import com.studydocs.review.domain.enums.InteractionType;
import com.studydocs.review.domain.enums.TargetType;
import com.studydocs.review.domain.model.InteractionModel;

import java.util.Optional;
import java.util.UUID;

/**
 * Interface Repository cho Interaction.
 */
public interface InteractionRepository {
    InteractionModel save(InteractionModel interaction);
    Optional<InteractionModel> findByUserIdAndTargetIdAndTargetType(UUID userId, UUID targetId, TargetType targetType);
    void delete(InteractionModel interaction);
    long countByTargetIdAndTargetTypeAndInteractionType(UUID targetId, TargetType targetType, InteractionType interactionType);
}
