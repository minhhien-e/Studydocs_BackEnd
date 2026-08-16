package com.studydocs.review.infrastructure.persistence;

import com.studydocs.review.domain.model.InteractionModel;
import com.studydocs.review.domain.model.ReviewModel;
import com.studydocs.review.infrastructure.persistence.entity.InteractionEntity;
import com.studydocs.review.infrastructure.persistence.entity.ReviewEntity;
import org.mapstruct.Mapper;

/**
 * Mapper chuyển đổi giữa Domain Model và Persistence Entity.
 */
@Mapper(componentModel = "spring")
public interface PersistenceMapper {
    
    ReviewEntity toEntity(ReviewModel domain);
    ReviewModel toDomain(ReviewEntity entity);
    
    InteractionEntity toEntity(InteractionModel domain);
    InteractionModel toDomain(InteractionEntity entity);
}
