package com.studydocs.review.application.dto;

import com.studydocs.review.domain.enums.InteractionType;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

/**
 * DTO yêu cầu thực hiện tương tác (Like/Dislike).
 */
@Getter
@Setter
public class InteractionRequest {
    @NotNull(message = "Loại tương tác không được để trống")
    private InteractionType type;
}
