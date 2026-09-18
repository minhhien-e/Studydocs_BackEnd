package com.studydocs.modules.review.service.impl;

import com.studydocs.modules.academic.entity.DocumentEntity;
import com.studydocs.modules.academic.repository.DocumentRepository;
import com.studydocs.modules.review.dto.ReviewDto;
import com.studydocs.modules.review.entity.DocumentReviewEntity;
import com.studydocs.modules.review.repository.ReviewRepository;
import com.studydocs.modules.review.service.ReviewService;
import com.studydocs.modules.user.repository.UserRepository;
import com.studydocs.modules.user.entity.UserEntity;
import com.studydocs.shared.exception.AppException;
import com.studydocs.shared.exception.ErrorCode;
import com.studydocs.modules.review.event.publisher.ReviewEventPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final DocumentRepository documentRepository;
    private final UserRepository userRepository;
    private final ReviewEventPublisher reviewEventPublisher;

    @Override
    public List<ReviewDto> getReviewsByDocument(String documentId) {
        return reviewRepository.findByDocumentId(documentId).stream()
                .filter(r -> r.getParentId() == null) // Only top-level comments
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public ReviewDto getReviewById(String reviewId) {
        DocumentReviewEntity review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new AppException(ErrorCode.REVIEW_NOT_FOUND));
        return toDto(review);
    }

    @Override
    public ReviewDto addReview(String userId, String documentId, Integer rating, String comment, String parentId) {
        DocumentReviewEntity review = DocumentReviewEntity.builder()
                .userId(userId)
                .documentId(documentId)
                .rating(rating)
                .comment(comment)
                .parentId(parentId)
                .build();

        review = reviewRepository.save(review);
        
        // Publish event để cập nhật số lượng comment bất đồng bộ
        reviewEventPublisher.publishReviewEvent(documentId, userId, true);
        
        return toDto(review);
    }

    @Override
    public ReviewDto updateReview(String reviewId, String comment) {
        DocumentReviewEntity review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new AppException(ErrorCode.REVIEW_NOT_FOUND));
        if (comment != null) {
            review.setComment(comment);
        }
        review = reviewRepository.save(review);
        return toDto(review);
    }

    @Override
    public void deleteReview(String reviewId) {
        DocumentReviewEntity review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new AppException(ErrorCode.REVIEW_NOT_FOUND));
        reviewRepository.delete(review);
        
        // Publish event để cập nhật số lượng comment bất đồng bộ
        reviewEventPublisher.publishReviewEvent(review.getDocumentId(), review.getUserId(), false);
    }

    @Override
    public List<ReviewDto> getReplies(String reviewId) {
        if (!reviewRepository.existsById(reviewId)) {
            throw new AppException(ErrorCode.REVIEW_NOT_FOUND);
        }
        return reviewRepository.findAll().stream()
                .filter(r -> reviewId.equals(r.getParentId()))
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    private ReviewDto toDto(DocumentReviewEntity r) {
        String[] userInfo = {null, null};
        
        if (r.getUserId() != null && !r.getUserId().equals("anonymous")) {
            userRepository.findById(r.getUserId()).ifPresent(user -> {
                userInfo[0] = user.getFullName();
                userInfo[1] = user.getAvatarUrl();
            });
        }
        
        long replyCount = reviewRepository.findAll().stream()
                .filter(child -> r.getId().equals(child.getParentId()))
                .count();

        return ReviewDto.builder()
                .id(r.getId())
                .documentId(r.getDocumentId())
                .userId(r.getUserId())
                .username(userInfo[0])
                .userAvatar(userInfo[1])
                .rating(r.getRating())
                .comment(r.getComment())
                .createdAt(r.getCreatedAt())
                .parentId(r.getParentId())
                .replyCount((int) replyCount)
                .likeCount(0)
                .build();
    }
}
