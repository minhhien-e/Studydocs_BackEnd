package com.studydocs.modules.review.service.impl;

import com.studydocs.modules.review.dto.ReviewDto;
import com.studydocs.modules.review.entity.DocumentReviewEntity;
import com.studydocs.modules.review.repository.ReviewRepository;
import com.studydocs.modules.review.service.ReviewService;
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
                .map(r -> ReviewDto.builder()
                        .id(r.getId())
                        .documentId(r.getDocumentId())
                        .userId(r.getUserId())
                        .rating(r.getRating())
                        .comment(r.getComment())
                        .createdAt(r.getCreatedAt())
                        .build())
                .collect(Collectors.toList());
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

        return ReviewDto.builder()
                .id(review.getId())
                .documentId(review.getDocumentId())
                .userId(review.getUserId())
                .rating(review.getRating())
                .comment(review.getComment())
                .createdAt(review.getCreatedAt())
                .build();
    }
}
