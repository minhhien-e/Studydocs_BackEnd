package com.studydocs.review.domain.model;

import com.studydocs.review.domain.valueobject.Rating;
import com.studydocs.review.domain.valueobject.ReviewContent;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Domain Model cho Bình luận (Review).
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewModel {
    private UUID id;
    private UUID documentId;
    private String documentTitle;
    private UUID userId;
    private String username;
    private String userAvatar;
    private ReviewContent content;
    private UUID parentId;
    @Builder.Default
    private List<ReviewModel> replies = new ArrayList<>();
    private Integer likeCount;
    private Integer dislikeCount;
    private Integer replyCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public void addReply(ReviewModel reply) {
        if (this.replies == null) {
            this.replies = new ArrayList<>();
        }
        this.replies.add(reply);
        this.replyCount++;
    }
}
