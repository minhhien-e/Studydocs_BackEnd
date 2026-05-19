package com.studydocs.review.application.usecase;

import com.studydocs.review.domain.model.ReviewModel;
import com.studydocs.review.domain.valueobject.ReviewContent;
import com.studydocs.review.domain.repository.ReviewRepository;
import com.studydocs.review.domain.exception.DuplicateReviewException;
import com.studydocs.review.domain.exception.ReviewNotFoundException;
import com.studydocs.review.domain.exception.UnauthorizedException;
import com.studydocs.review.application.dto.DocumentStatistics;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import com.studydocs.review.domain.repository.InteractionRepository;

/**
 * Lớp UseCase xử lý nghiệp vụ cho Bình luận.
 */
@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final InteractionRepository interactionRepository;

    @Transactional
    public ReviewModel createReview(UUID documentId, String documentTitle, UUID userId, String username, String avatar, String content) {
        reviewRepository.findByDocumentIdAndUserIdAndParentIsNull(documentId, userId)
                .ifPresent(r -> { throw new DuplicateReviewException(); });

        ReviewModel review = ReviewModel.builder()
                .documentId(documentId)
                .documentTitle(documentTitle)
                .userId(userId)
                .username(username)
                .userAvatar(avatar)
                .content(new ReviewContent(content))
                .likeCount(0)
                .dislikeCount(0)
                .replyCount(0)
                .build();

        return reviewRepository.save(review);
    }

    @Transactional
    public ReviewModel replyToReview(UUID parentId, UUID userId, String username, String avatar, String content) {
        ReviewModel parent = reviewRepository.findById(parentId)
                .orElseThrow(ReviewNotFoundException::new);

        ReviewModel reply = ReviewModel.builder()
                .documentId(parent.getDocumentId())
                .userId(userId)
                .username(username)
                .userAvatar(avatar)
                .content(new ReviewContent(content))
                .parentId(parentId)
                .likeCount(0)
                .dislikeCount(0)
                .replyCount(0)
                .build();

        parent.addReply(reply);
        reviewRepository.save(parent);
        
        return reviewRepository.save(reply);
    }

    public Page<ReviewModel> getDocumentReviews(UUID documentId, Pageable pageable) {
        return reviewRepository.findByDocumentIdAndParentIsNull(documentId, pageable);
    }

    @Transactional
    public void deleteReview(UUID reviewId, UUID userId) {
        ReviewModel review = reviewRepository.findById(reviewId)
                .orElseThrow(ReviewNotFoundException::new);
        
        if (!review.getUserId().equals(userId)) {
            throw new UnauthorizedException();
        }

        reviewRepository.delete(review);
    }

    public ReviewModel getReviewById(UUID id) {
        return reviewRepository.findById(id).orElseThrow(ReviewNotFoundException::new);
    }

    @Transactional
    public ReviewModel updateReview(UUID id, UUID userId, String content) {
        ReviewModel review = reviewRepository.findById(id).orElseThrow(ReviewNotFoundException::new);
        if (!review.getUserId().equals(userId)) {
            throw new UnauthorizedException();
        }
        review.setContent(new ReviewContent(content));
        return reviewRepository.save(review);
    }

    public Page<ReviewModel> getReplies(UUID parentId, Pageable pageable) {
        return reviewRepository.findByParentId(parentId, pageable);
    }

    public DocumentStatistics getDocumentStatistics(UUID documentId) {
        Long totalReviews = reviewRepository.countByDocumentId(documentId);
        long totalLikes = interactionRepository.countByTargetIdAndTargetTypeAndInteractionType(
                documentId, com.studydocs.review.domain.enums.TargetType.DOCUMENT, com.studydocs.review.domain.enums.InteractionType.LIKE);
        long totalDislikes = interactionRepository.countByTargetIdAndTargetTypeAndInteractionType(
                documentId, com.studydocs.review.domain.enums.TargetType.DOCUMENT, com.studydocs.review.domain.enums.InteractionType.DISLIKE);
        
        return new DocumentStatistics(
                totalReviews, 
                totalLikes, 
                totalDislikes);
    }

    public Page<ReviewModel> getUserReviews(UUID userId, Pageable pageable) {
        return reviewRepository.findByUserId(userId, pageable);
    }
}
