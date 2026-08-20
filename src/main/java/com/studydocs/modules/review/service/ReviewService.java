package com.studydocs.modules.review.service;

import com.studydocs.modules.review.dto.ReviewDto;

import java.util.List;

public interface ReviewService {
    List<ReviewDto> getReviewsByDocument(String documentId);
    ReviewDto getReviewById(String reviewId);
    ReviewDto addReview(String userId, String documentId, Integer rating, String comment);
    ReviewDto updateReview(String reviewId, String comment);
    void deleteReview(String reviewId);
    List<ReviewDto> getReplies(String reviewId);
}
