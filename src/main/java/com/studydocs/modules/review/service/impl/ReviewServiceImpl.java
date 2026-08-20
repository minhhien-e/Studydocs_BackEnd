package com.studydocs.modules.review.service.impl;

import com.studydocs.modules.review.dto.ReviewDto;
import com.studydocs.modules.review.entity.DocumentReviewEntity;
import com.studydocs.modules.review.repository.ReviewRepository;
import com.studydocs.modules.review.service.ReviewService;
import com.studydocs.shared.exception.AppException;
import com.studydocs.shared.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;

    @Override
    public List<ReviewDto> getReviewsByDocument(String documentId) {
        return reviewRepository.findByDocumentId(documentId).stream()
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
    public ReviewDto addReview(String userId, String documentId, Integer rating, String comment) {
        DocumentReviewEntity review = DocumentReviewEntity.builder()
                .userId(userId)
                .documentId(documentId)
                .rating(rating)
                .comment(comment)
                .build();

        review = reviewRepository.save(review);
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
    }

    @Override
    public List<ReviewDto> getReplies(String reviewId) {
        if (!reviewRepository.existsById(reviewId)) {
            throw new AppException(ErrorCode.REVIEW_NOT_FOUND);
        }
        return reviewRepository.findAll().stream()
                .filter(r -> reviewId.equals(r.getDocumentId()))
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    private ReviewDto toDto(DocumentReviewEntity r) {
        return ReviewDto.builder()
                .id(r.getId())
                .documentId(r.getDocumentId())
                .userId(r.getUserId())
                .rating(r.getRating())
                .comment(r.getComment())
                .createdAt(r.getCreatedAt())
                .build();
    }
}
