package com.example.educationservice.document.dto.request;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

public record UpdateDocumentDisplayMetadataRequest(
        @Size(max = 1000, message = "thumbnail must not exceed 1000 characters")
        String thumbnail,

        @PositiveOrZero(message = "pageCount must be greater than or equal to 0")
        Integer pageCount
) {
    @AssertTrue(message = "thumbnail or pageCount must be provided")
    public boolean hasAtLeastOneField() {
        return thumbnail != null || pageCount != null;
    }
}
