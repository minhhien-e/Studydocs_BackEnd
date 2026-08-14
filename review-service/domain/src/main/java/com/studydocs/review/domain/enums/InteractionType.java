package com.studydocs.review.domain.enums;

import lombok.Getter;

/**
 * Loại tương tác (Thích hoặc Không thích)
 */
@Getter
public enum InteractionType {
    LIKE("Thích"),
    DISLIKE("Không thích");

    private final String description;

    InteractionType(String description) {
        this.description = description;
    }
}
