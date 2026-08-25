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
        
        documentRepository.findById(documentId).ifPresent(doc -> {
            doc.setCommentCount((doc.getCommentCount() != null ? doc.getCommentCount() : 0) + 1);
            documentRepository.save(doc);
        });
        
        if (userId != null && !"anonymous".equals(userId)) {
            userRepository.findById(userId).ifPresent(user -> {
                user.setCommentsCount((user.getCommentsCount() != null ? user.getCommentsCount() : 0) + 1);
                userRepository.save(user);
            });
        }
        
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
        
        documentRepository.findById(review.getDocumentId()).ifPresent(doc -> {
            int currentCount = doc.getCommentCount() != null ? doc.getCommentCount() : 0;
            doc.setCommentCount(Math.max(0, currentCount - 1));
            documentRepository.save(doc);
        });
        
        if (review.getUserId() != null && !"anonymous".equals(review.getUserId())) {
            userRepository.findById(review.getUserId()).ifPresent(user -> {
                int currentComments = user.getCommentsCount() != null ? user.getCommentsCount() : 0;
                user.setCommentsCount(Math.max(0, currentComments - 1));
                userRepository.save(user);
            });
        }
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
        String[] userInfo = {"Anonymous", "https://ui-avatars.com/api/?name=User"};
        
        if (r.getUserId() != null && !r.getUserId().equals("anonymous")) {
            userRepository.findById(r.getUserId()).ifPresent(user -> {
                userInfo[0] = user.getFullName() != null ? user.getFullName() : "Anonymous";
                userInfo[1] = user.getAvatarUrl() != null ? user.getAvatarUrl() : "https://ui-avatars.com/api/?name=" + userInfo[0];
            });
        }
        
        return ReviewDto.builder()
                .id(r.getId())
                .documentId(r.getDocumentId())
                .userId(r.getUserId())
                .username(userInfo[0])
                .userAvatar(userInfo[1])
                .rating(r.getRating())
                .comment(r.getComment())
                .createdAt(r.getCreatedAt())
                .build();
    }
}
