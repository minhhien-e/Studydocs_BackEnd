package com.example.educationservice.document.dto.request;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.PositiveOrZero;

public record UpdateDocumentStatsRequest(
        @PositiveOrZero(message = "likeCount must be greater than or equal to 0")
        Long likeCount,

        @PositiveOrZero(message = "commentCount must be greater than or equal to 0")
        Long commentCount
) {
    @AssertTrue(message = "likeCount or commentCount must be provided")
    public boolean hasAtLeastOneField() {
        return likeCount != null || commentCount != null;
    }
}
