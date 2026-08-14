package com.studydocs.review.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

/**
 * DTO yêu cầu tạo mới một bình luận.
 */
@Getter
@Setter
public class ReviewRequest {
    @NotNull(message = "DocumentId không được để trống")
    private UUID documentId;
    
    @NotBlank(message = "Nội dung không được để trống")
    private String content;

    private String documentTitle; // Lưu snapshot tên tài liệu
}
