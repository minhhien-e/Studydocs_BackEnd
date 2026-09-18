package com.studydocs.modules.academic.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DocumentSummaryDto {
    private String id;
    private String title;
    private String description;
    private String fileUrl;
    private Long fileSize;
    private String fileType;
    private String uploaderId;
    private String uploaderName;
    private String uploaderAvatarUrl;

    // FE DocumentSummaryModel specific fields
    private String thumbnail;
    private String category;
    private String school;
    
    @Builder.Default
    private Integer pageCount = 10;
    
    @Builder.Default
    private String year = "2024";

    private Long universityId;
    private String universityName;
    private Long facultyId;
    private Long departmentId;
    private Long subjectId;
    
    @Builder.Default
    private Integer likeCount = 0;

    @Builder.Default
    private Integer commentCount = 0;

    @Builder.Default
    private Integer downloadCount = 0;

    @Builder.Default
    private Integer viewCount = 0;

    @Builder.Default
    private Boolean isLiked = false;

    @Builder.Default
    private Integer dislikeCount = 0;

    @Builder.Default
    private Boolean isDisliked = false;

    @Builder.Default
    private Boolean isBookmarked = false;

    @Builder.Default
    private Boolean isPublic = true;
    
    private String status;

    private LocalDateTime createdAt;
}
