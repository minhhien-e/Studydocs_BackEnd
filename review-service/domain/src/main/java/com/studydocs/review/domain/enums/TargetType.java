package com.studydocs.review.domain.enums;

import lombok.Getter;

/**
 * Đối tượng của tương tác (Bình luận hoặc Tài liệu)
 */
@Getter
public enum TargetType {
    REVIEW("Bình luận"),
    DOCUMENT("Tài liệu");

    private final String description;

    TargetType(String description) {
        this.description = description;
    }
}
