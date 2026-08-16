package com.studydocs.modules.review.service;

import com.studydocs.modules.review.dto.ReviewDto;

import java.util.List;

public interface ReviewService {
    List<ReviewDto> getReviewsByDocument(String documentId);
    ReviewDto addReview(String userId, String documentId, Integer rating, String comment);
}
