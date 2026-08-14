package com.studydocs.review.infrastructure.persistence.entity;

import com.studydocs.review.domain.valueobject.Rating;
import com.studydocs.review.domain.valueobject.ReviewContent;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * JPA Entity cho Bình luận.
 * Chứa các annotation ánh xạ vào database MySQL.
 */
@Entity
@Table(name = "reviews")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private UUID documentId;

    private String documentTitle;

    @Column(nullable = false)
    private UUID userId;

    private String username;

    private String userAvatar;

    @Embedded
    private ReviewContent content;

    @Column(name = "parent_id")
    private UUID parentId;

    @Builder.Default
    private Integer likeCount = 0;

    @Builder.Default
    private Integer dislikeCount = 0;

    @Builder.Default
    private Integer replyCount = 0;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;
}
