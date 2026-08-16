package com.studydocs.modules.review.controller;

import com.studydocs.modules.review.dto.ReviewDto;
import com.studydocs.modules.review.service.ReviewService;
import com.studydocs.shared.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @GetMapping({"/user/reviews/document", "/documents/{documentId}/reviews"})
    public ApiResponse<List<ReviewDto>> getReviews(@RequestParam(value = "documentId", required = false) String paramDocId,
                                                   @PathVariable(required = false) String documentId) {
        String docId = documentId != null ? documentId : paramDocId;
        return ApiResponse.success(reviewService.getReviewsByDocument(docId));
    }

    @PostMapping({"/user/reviews/document", "/reviews"})
    public ApiResponse<ReviewDto> addReview(Authentication authentication, @RequestBody Map<String, Object> body) {
        String userId = authentication != null ? authentication.getName() : "anonymous";
        String documentId = (String) body.getOrDefault("documentId", "doc-001");
        Integer rating = body.get("rating") != null ? (Integer) body.get("rating") : 5;
        String comment = (String) body.getOrDefault("comment", body.get("content"));

        return ApiResponse.success(reviewService.addReview(userId, documentId, rating, comment));
    }

    @PutMapping("/reviews/{commentId}")
    public ApiResponse<ReviewDto> updateReview(@PathVariable String commentId, @RequestBody Map<String, Object> body) {
        String content = (String) body.getOrDefault("content", "Updated comment content");
        return ApiResponse.success(ReviewDto.builder()
                .id(commentId)
                .documentId("doc-001")
                .userId("usr-admin-001")
                .rating(5)
                .comment(content)
                .createdAt(LocalDateTime.now())
                .build());
    }

    @DeleteMapping("/reviews/{commentId}")
    public ApiResponse<String> deleteReview(@PathVariable String commentId) {
        return ApiResponse.success("Review deleted successfully");
    }

    @GetMapping("/reviews/{commentId}/replies")
    public ApiResponse<List<ReviewDto>> getReplies(@PathVariable String commentId) {
        return ApiResponse.success(List.of(
                ReviewDto.builder()
                        .id(UUID.randomUUID().toString())
                        .documentId("doc-001")
                        .userId("usr-user-002")
                        .rating(5)
                        .comment("Cảm ơn bạn đã chia sẻ tài liệu rất hay!")
                        .createdAt(LocalDateTime.now())
                        .build()
        ));
    }

    @PostMapping("/reviews/{commentId}/replies")
    public ApiResponse<ReviewDto> addReply(@PathVariable String commentId, Authentication authentication, @RequestBody Map<String, Object> body) {
        String userId = authentication != null ? authentication.getName() : "anonymous";
        String content = (String) body.getOrDefault("content", "Phản hồi bài viết");
        return ApiResponse.success(ReviewDto.builder()
                .id(UUID.randomUUID().toString())
                .documentId("doc-001")
                .userId(userId)
                .rating(5)
                .comment(content)
                .createdAt(LocalDateTime.now())
                .build());
    }

    @PostMapping("/reviews/{commentId}/interactions")
    public ApiResponse<Map<String, Object>> interactWithReview(@PathVariable String commentId, @RequestBody Map<String, Object> body) {
        String type = (String) body.getOrDefault("type", "LIKE");
        return ApiResponse.success(Map.of(
                "commentId", commentId,
                "type", type,
                "status", "success"
        ));
    }

    @PostMapping("/documents/{documentId}/interactions")
    public ApiResponse<Map<String, Object>> interactWithDocument(@PathVariable String documentId, @RequestBody Map<String, Object> body) {
        String type = (String) body.getOrDefault("type", "LIKE");
        return ApiResponse.success(Map.of(
                "documentId", documentId,
                "type", type,
                "status", "success"
        ));
    }

    @GetMapping("/user/reviews/admin/stats/reactions/total-likes")
    public ApiResponse<Map<String, Object>> getAdminTotalLikes() {
        return ApiResponse.success(Map.of("totalLikes", 1520));
    }

    @GetMapping("/user/reviews/admin/stats/reviews/total")
    public ApiResponse<Map<String, Object>> getAdminTotalReviews() {
        return ApiResponse.success(Map.of("totalReviews", 430));
    }

    @GetMapping("/user/reviews/user/me/reactions/count")
    public ApiResponse<Map<String, Object>> getMyReactionCount(Authentication authentication) {
        return ApiResponse.success(Map.of("reactionCount", 18));
    }

    @GetMapping("/user/reviews/user/{userId}/count")
    public ApiResponse<Map<String, Object>> getUserReviewCount(@PathVariable String userId) {
        return ApiResponse.success(Map.of("reviewCount", 5, "userId", userId));
    }
}
