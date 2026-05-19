package com.studydocs.review.application.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * DTO phản hồi chứa thông tin chi tiết về bình luận.
 */
@Getter
@Builder
public class ReviewResponse {
    private UUID id;
    private UUID documentId;
    private String documentTitle;
    private UUID userId;
    private String username;
    private String userAvatar;
    private String content; // Nội dung từ Value Object Content
    private UUID parentId;
    private Integer likeCount;
    private Integer dislikeCount;
    private Integer replyCount;
    private LocalDateTime createdAt;
    private List<ReviewResponse> replies;
}
