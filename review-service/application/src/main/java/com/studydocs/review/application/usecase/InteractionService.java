package com.studydocs.review.application.usecase;

import com.studydocs.review.domain.enums.InteractionType;
import com.studydocs.review.domain.enums.TargetType;
import com.studydocs.review.domain.model.InteractionModel;
import com.studydocs.review.domain.model.ReviewModel;
import com.studydocs.review.domain.repository.InteractionRepository;
import com.studydocs.review.domain.repository.ReviewRepository;
import com.studydocs.review.domain.exception.ReviewNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

/**
 * Lớp UseCase xử lý tương tác Like/Dislike.
 */
@Service
@RequiredArgsConstructor
public class InteractionService {

    private final InteractionRepository interactionRepository;
    private final ReviewRepository reviewRepository;
    private final DocumentStatsSyncService documentStatsSyncService;

    @Transactional
    public void interact(UUID userId, UUID targetId, TargetType targetType, InteractionType type) {
        Optional<InteractionModel> existing = interactionRepository.findByUserIdAndTargetIdAndTargetType(userId, targetId, targetType);

        if (existing.isPresent()) {
            InteractionModel interaction = existing.get();
            if (interaction.getInteractionType() == type) {
                interactionRepository.delete(interaction);
                updateCounts(targetId, targetType, type, -1);
            } else {
                InteractionType oldType = interaction.getInteractionType();
                interaction.setInteractionType(type);
                interactionRepository.save(interaction);
                updateCounts(targetId, targetType, oldType, -1);
                updateCounts(targetId, targetType, type, 1);
            }
        } else {
            InteractionModel interaction = InteractionModel.builder()
                    .userId(userId)
                    .targetId(targetId)
                    .targetType(targetType)
                    .interactionType(type)
                    .build();
            interactionRepository.save(interaction);
            updateCounts(targetId, targetType, type, 1);
        }
        if (targetType == TargetType.DOCUMENT) {
            documentStatsSyncService.syncStats(targetId);
        }
    }

    private void updateCounts(UUID targetId, TargetType targetType, InteractionType type, int delta) {
        if (targetType == TargetType.REVIEW) {
            ReviewModel review = reviewRepository.findById(targetId)
                    .orElseThrow(ReviewNotFoundException::new);
            
            if (type == InteractionType.LIKE) {
                review.setLikeCount(Math.max(0, review.getLikeCount() + delta));
            } else {
                review.setDislikeCount(Math.max(0, review.getDislikeCount() + delta));
            }
            reviewRepository.save(review);
        }
    }
}
