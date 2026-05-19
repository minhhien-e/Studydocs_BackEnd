package com.studydocs.review.infrastructure.persistence.repository;

import com.studydocs.review.domain.enums.TargetType;
import com.studydocs.review.domain.model.InteractionModel;
import com.studydocs.review.domain.repository.InteractionRepository;
import com.studydocs.review.infrastructure.persistence.PersistenceMapper;
import com.studydocs.review.infrastructure.persistence.entity.InteractionEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

/**
 * Triển khai InteractionRepository (Domain) sử dụng Spring Data JPA.
 */
@Component
@RequiredArgsConstructor
public class InteractionRepositoryImpl implements InteractionRepository {

    private final InteractionJpaRepository jpaRepository;
    private final PersistenceMapper mapper;

    @Override
    public InteractionModel save(InteractionModel interaction) {
        InteractionEntity entity = mapper.toEntity(interaction);
        InteractionEntity saved = jpaRepository.save(entity);
        return mapper.toDomain(saved);
    }

    @Override
    public Optional<InteractionModel> findByUserIdAndTargetIdAndTargetType(UUID userId, UUID targetId, TargetType targetType) {
        return jpaRepository.findByUserIdAndTargetIdAndTargetType(userId, targetId, targetType)
                .map(mapper::toDomain);
    }

    @Override
    public void delete(InteractionModel interaction) {
        jpaRepository.delete(mapper.toEntity(interaction));
    }

    @Override
    public long countByTargetIdAndTargetTypeAndInteractionType(UUID targetId, TargetType targetType, com.studydocs.review.domain.enums.InteractionType interactionType) {
        return jpaRepository.countByTargetIdAndTargetTypeAndInteractionType(targetId, targetType, interactionType);
    }
}
