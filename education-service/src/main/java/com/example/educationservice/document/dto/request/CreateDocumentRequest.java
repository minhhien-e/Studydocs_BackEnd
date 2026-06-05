package com.example.educationservice.document.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateDocumentRequest(
        @NotNull(message = "universityId must not be null")
        Long universityId,

        @NotNull(message = "subjectId must not be null")
        Long subjectId,

        @NotBlank(message = "title must not be blank")
        @Size(max = 255, message = "title must be <= 255 characters")
        String title,

        Integer year,

        String description
) {
}
