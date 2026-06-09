package com.example.educationservice.document.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record MediaCallbackRequest(
        @NotBlank(message = "documentUuid must not be blank")
        String documentUuid,

        @NotNull(message = "mediaId must not be null")
        Long mediaId
) {
}
