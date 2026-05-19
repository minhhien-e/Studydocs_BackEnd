package com.studydocs.review.infrastructure.persistence.entity;

import com.studydocs.review.domain.enums.InteractionType;
import com.studydocs.review.domain.enums.TargetType;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

/**
 * JPA Entity cho Tương tác.
 */
@Entity
@Table(name = "interactions", 
       uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "target_id", "target_type"}))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InteractionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Column(name = "target_id", nullable = false)
    private UUID targetId;

    @Enumerated(EnumType.STRING)
    @Column(name = "target_type", nullable = false)
    private TargetType targetType;

    @Enumerated(EnumType.STRING)
    @Column(name = "interaction_type", nullable = false)
    private InteractionType interactionType;
}
