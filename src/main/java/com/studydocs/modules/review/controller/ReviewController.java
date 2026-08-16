package com.studydocs.modules.review.controller;

import com.studydocs.modules.review.dto.ReviewDto;
import com.studydocs.modules.review.service.ReviewService;
import com.studydocs.shared.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @GetMapping("/document")
    public ApiResponse<List<ReviewDto>> getReviews(@RequestParam("documentId") String documentId) {
        return ApiResponse.success(reviewService.getReviewsByDocument(documentId));
    }

    @PostMapping("/document")
    public ApiResponse<ReviewDto> addReview(Authentication authentication, @RequestBody Map<String, Object> body) {
        String userId = authentication != null ? authentication.getName() : "anonymous";
        String documentId = (String) body.get("documentId");
        Integer rating = body.get("rating") != null ? (Integer) body.get("rating") : 5;
        String comment = (String) body.get("comment");

        return ApiResponse.success(reviewService.addReview(userId, documentId, rating, comment));
    }

    @GetMapping("/admin/stats/reactions/total-likes")
    public ApiResponse<Map<String, Object>> getAdminTotalLikes() {
        return ApiResponse.success(Map.of("totalLikes", 1520));
    }

    @GetMapping("/admin/stats/reviews/total")
    public ApiResponse<Map<String, Object>> getAdminTotalReviews() {
        return ApiResponse.success(Map.of("totalReviews", 430));
    }

    @GetMapping("/user/me/reactions/count")
    public ApiResponse<Map<String, Object>> getMyReactionCount(Authentication authentication) {
        return ApiResponse.success(Map.of("reactionCount", 18));
    }

    @GetMapping("/user/{userId}/count")
    public ApiResponse<Map<String, Object>> getUserReviewCount(@PathVariable String userId) {
        return ApiResponse.success(Map.of("reviewCount", 5, "userId", userId));
    }
}
