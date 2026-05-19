package com.studydocs.review.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

/**
 * DTO yêu cầu phản hồi một bình luận.
 */
@Getter
@Setter
public class ReplyRequest {
    @NotBlank(message = "Nội dung phản hồi không được để trống")
    private String content;
}
