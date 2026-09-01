package com.studydocs.modules.academic.entity;

import com.studydocs.shared.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "documents")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DocumentEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false, length = 300)
    private String title;

    @Column(length = 2000)
    private String description;

    @Column(name = "file_url", length = 1000)
    private String fileUrl;

    @Column(name = "file_size", nullable = false, columnDefinition = "bigint default 0")
    @Builder.Default
    private Long fileSize = 0L;

    @Column(name = "file_type", length = 50)
    private String fileType;

    @Column(name = "uploader_id", nullable = false)
    private String uploaderId;

    @Column(name = "university_id")
    private Long universityId;

    @Column(name = "faculty_id")
    private Long facultyId;

    @Column(name = "subject_id")
    private Long subjectId;

    @Column(name = "department_id")
    private Long departmentId;

    @Column(name = "school_year", length = 20, columnDefinition = "varchar(20) default ''")
    @Builder.Default
    private String schoolYear = "";

    @Column(name = "status", length = 30)
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private DocumentStatus status = DocumentStatus.PENDING;

    @Column(name = "like_count", nullable = false, columnDefinition = "integer default 0")
    @Builder.Default
    private Integer likeCount = 0;

    @Column(name = "download_count", nullable = false, columnDefinition = "integer default 0")
    @Builder.Default
    private Integer downloadCount = 0;

    @Column(name = "view_count", nullable = false, columnDefinition = "integer default 0")
    @Builder.Default
    private Integer viewCount = 0;

    @Column(name = "comment_count", nullable = false, columnDefinition = "integer default 0")
    @Builder.Default
    private Integer commentCount = 0;

    @Column(name = "is_public", nullable = false, columnDefinition = "boolean default true")
    @Builder.Default
    private Boolean isPublic = true;

    @Column(name = "page_count", nullable = false, columnDefinition = "integer default 0")
    @Builder.Default
    private Integer pageCount = 0;
}
