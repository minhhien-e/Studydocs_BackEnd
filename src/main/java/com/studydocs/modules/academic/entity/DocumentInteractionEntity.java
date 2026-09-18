package com.studydocs.modules.academic.entity;

import com.studydocs.shared.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "document_interactions")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DocumentInteractionEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "document_id", nullable = false)
    private String documentId;

    @Column(name = "user_id", nullable = false)
    private String userId;

    @Column(name = "interaction_type", nullable = false, length = 20)
    private String type; // e.g. "LIKE", "BOOKMARK"
}
