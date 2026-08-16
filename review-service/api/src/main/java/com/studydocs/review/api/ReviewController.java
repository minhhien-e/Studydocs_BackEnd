package com.studydocs.review.api;

import com.studydocs.review.application.usecase.InteractionService;
import com.studydocs.review.application.usecase.ReviewService;
import com.studydocs.review.domain.enums.TargetType;
import com.studydocs.review.domain.model.ReviewModel;
import com.studydocs.review.application.dto.*;
import com.studydocs.review.application.mapper.ReviewMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.studydocs.review.domain.exception.UnauthorizedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.UUID;

/**
 * Controller xử lý các yêu cầu REST API cho Bình luận.
 */
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;
    private final InteractionService interactionService;
    private final ReviewMapper reviewMapper;

    @PostMapping("/reviews")
    public ResponseEntity<ReviewResponse> createReview(
            @Valid @RequestBody ReviewRequest request) {
        
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !(auth.getPrincipal() instanceof Jwt jwt)) {
            throw new UnauthorizedException();
        }

        UUID userId = UUID.fromString(jwt.getSubject());
        String username = jwt.getClaimAsString("preferred_username");
        if (username == null) {
            username = jwt.getClaimAsString("username");
        }
        if (username == null) {
            username = jwt.getClaimAsString("name");
        }
        String avatar = jwt.getClaimAsString("avatar_url");
        if (avatar == null) {
            avatar = jwt.getClaimAsString("picture");
        }
        if (avatar == null) {
            avatar = "https://ui-avatars.com/api/?name=" + username;
        }

        ReviewModel review = reviewService.createReview(
                request.getDocumentId(),
                request.getDocumentTitle(),
                userId,
                username,
                avatar,
                request.getContent()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(reviewMapper.toResponse(review));
    }

    @PostMapping("/reviews/{id}/replies")
    public ResponseEntity<ReviewResponse> replyToReview(
            @PathVariable UUID id,
            @Valid @RequestBody ReplyRequest request) {
        
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !(auth.getPrincipal() instanceof Jwt jwt)) {
            throw new UnauthorizedException();
        }

        UUID userId = UUID.fromString(jwt.getSubject());
        String username = jwt.getClaimAsString("preferred_username");
        if (username == null) {
            username = jwt.getClaimAsString("username");
        }
        if (username == null) {
            username = jwt.getClaimAsString("name");
        }
        String avatar = jwt.getClaimAsString("avatar_url");
        if (avatar == null) {
            avatar = jwt.getClaimAsString("picture");
        }
        if (avatar == null) {
            avatar = "https://ui-avatars.com/api/?name=" + username;
        }

        ReviewModel reply = reviewService.replyToReview(
                id, 
                userId, 
                username,
                avatar,
                request.getContent()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(reviewMapper.toResponse(reply));
    }

    @GetMapping("/reviews/{id}")
    public ResponseEntity<ReviewResponse> getReview(@PathVariable UUID id) {
        ReviewModel review = reviewService.getReviewById(id);
        return ResponseEntity.ok(reviewMapper.toResponse(review));
    }

    @PutMapping("/reviews/{id}")
    public ResponseEntity<ReviewResponse> updateReview(
            @PathVariable UUID id,
            @Valid @RequestBody ReviewUpdateRequest request) {
        
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !(auth.getPrincipal() instanceof Jwt jwt)) {
            throw new UnauthorizedException();
        }

        UUID userId = UUID.fromString(jwt.getSubject());
        ReviewModel review = reviewService.updateReview(id, userId, request.getContent());
        return ResponseEntity.ok(reviewMapper.toResponse(review));
    }

    @GetMapping("/reviews/{id}/replies")
    public ResponseEntity<Page<ReviewResponse>> getReviewReplies(
            @PathVariable UUID id,
            Pageable pageable) {
        Page<ReviewModel> replies = reviewService.getReplies(id, pageable);
        return ResponseEntity.ok(replies.map(reviewMapper::toResponse));
    }

    @GetMapping("/documents/{documentId}/reviews")
    public ResponseEntity<Page<ReviewResponse>> getDocumentReviews(
            @PathVariable UUID documentId,
            Pageable pageable) {
        Page<ReviewModel> reviews = reviewService.getDocumentReviews(documentId, pageable);
        return ResponseEntity.ok(reviews.map(reviewMapper::toResponse));
    }

    @GetMapping("/documents/{documentId}/statistics")
    public ResponseEntity<DocumentStatistics> getDocumentStatistics(@PathVariable UUID documentId) {
        return ResponseEntity.ok(reviewService.getDocumentStatistics(documentId));
    }

    @GetMapping("/users/me/reviews")
    public ResponseEntity<Page<ReviewResponse>> getMyReviews(Pageable pageable) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !(auth.getPrincipal() instanceof Jwt jwt)) {
            throw new UnauthorizedException();
        }

        UUID userId = UUID.fromString(jwt.getSubject());
        return ResponseEntity.ok(reviewService.getUserReviews(userId, pageable).map(reviewMapper::toResponse));
    }

    @GetMapping("/users/{userId}/reviews")
    public ResponseEntity<Page<ReviewResponse>> getReviewsByUser(
            @PathVariable UUID userId,
            Pageable pageable) {
        Page<ReviewModel> reviews = reviewService.getUserReviews(userId, pageable);
        return ResponseEntity.ok(reviews.map(reviewMapper::toResponse));
    }

    @PostMapping("/reviews/{id}/interactions")
    public ResponseEntity<Void> interactWithReview(
            @PathVariable UUID id,
            @Valid @RequestBody InteractionRequest request) {
        
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !(auth.getPrincipal() instanceof Jwt jwt)) {
            throw new UnauthorizedException();
        }

        UUID userId = UUID.fromString(jwt.getSubject());
        interactionService.interact(userId, id, TargetType.REVIEW, request.getType());
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/documents/{documentId}/interactions")
    public ResponseEntity<Void> interactWithDocument(
            @PathVariable UUID documentId,
            @Valid @RequestBody InteractionRequest request) {
        
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !(auth.getPrincipal() instanceof Jwt jwt)) {
            throw new UnauthorizedException();
        }

        UUID userId = UUID.fromString(jwt.getSubject());
        interactionService.interact(userId, documentId, TargetType.DOCUMENT, request.getType());
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/reviews/{id}")
    public ResponseEntity<Void> deleteReview(@PathVariable UUID id) {
        
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !(auth.getPrincipal() instanceof Jwt jwt)) {
            throw new UnauthorizedException();
        }

        UUID userId = UUID.fromString(jwt.getSubject());
        reviewService.deleteReview(id, userId);
        return ResponseEntity.noContent().build();
    }
}
