package com.example.educationservice.document.dto.response;

public record DocumentListItemResponse(
        String id,
        String title,
        String thumbnail,
        String category,
        String school,
        Integer pageCount,
        String year,
        Long likeCount,
        Long commentCount,
        boolean isLiked,
        boolean isBookmarked
) {
}
