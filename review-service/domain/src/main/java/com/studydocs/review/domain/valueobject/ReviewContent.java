package com.studydocs.review.domain.valueobject;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Value Object đại diện cho nội dung của bình luận
 */
@Embeddable
@Getter
@NoArgsConstructor
public class ReviewContent {
    private String content;

    public ReviewContent(String content) {
        if (content == null || content.trim().isEmpty()) {
            throw new IllegalArgumentException("Nội dung bình luận không được để trống");
        }
        if (content.length() > 2000) {
            throw new IllegalArgumentException("Nội dung bình luận không được quá 2000 ký tự");
        }
        this.content = content;
    }
}
