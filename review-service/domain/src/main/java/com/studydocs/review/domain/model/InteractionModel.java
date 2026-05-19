package com.studydocs.review.domain.model;

import com.studydocs.review.domain.enums.InteractionType;
import com.studydocs.review.domain.enums.TargetType;
import lombok.*;

import java.util.UUID;

/**
 * Domain Model cho Tương tác (Interaction).
  Like/Dislike.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InteractionModel {
    private UUID id;
    private UUID userId;
    private UUID targetId;
    private TargetType targetType;
    private InteractionType interactionType;
}
