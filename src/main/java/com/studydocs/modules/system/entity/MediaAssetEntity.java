package com.studydocs.modules.system.entity;

import com.studydocs.shared.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "media_assets")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MediaAssetEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "file_name", nullable = false)
    private String fileName;

    @Column(name = "file_url", nullable = false)
    private String fileUrl;

    @Column(name = "content_type")
    private String contentType;

    @Column(name = "file_size")
    private Long fileSize;

    @Column(name = "owner_id")
    private String ownerId;
}
