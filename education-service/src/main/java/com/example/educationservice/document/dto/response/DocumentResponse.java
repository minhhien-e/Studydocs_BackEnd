package com.example.educationservice.document.dto.response;

import java.time.LocalDateTime;

public record DocumentResponse(
        String uuid,
        String title,
        Integer year,
        String description,
        String status,
        Long mediaId,
        String thumbnail,
        Integer pageCount,
        Long likeCount,
        Long commentCount,
        Long universityId,
        Long subjectId,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
