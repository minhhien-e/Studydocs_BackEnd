package com.example.educationservice.document.repository;

import com.example.educationservice.document.entity.DocumentEntity;
import com.example.educationservice.document.entity.DocumentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface DocumentRepository extends JpaRepository<DocumentEntity, Long> {

    Optional<DocumentEntity> findByUuid(UUID uuid);

    boolean existsByUuid(UUID uuid);

    @Query(
            value = """
                    select d.uuid as uuid,
                           d.title as title,
                           d.thumbnailUrl as thumbnail,
                           s.name as category,
                           u.name as school,
                           d.pageCount as pageCount,
                           d.year as documentYear,
                           d.likeCount as likeCount,
                           d.commentCount as commentCount
                    from DocumentEntity d
                    join SubjectEntity s on s.id = d.subjectId
                    join UniversityEntity u on u.id = d.universityId
                    where d.status = :status
                    order by d.createdAt desc
                    """,
            countQuery = """
                    select count(d)
                    from DocumentEntity d
                    join SubjectEntity s on s.id = d.subjectId
                    join UniversityEntity u on u.id = d.universityId
                    where d.status = :status
                    """
    )
    Page<DocumentListProjection> findDocumentListByStatus(@Param("status") DocumentStatus status, Pageable pageable);

    interface DocumentListProjection {
        UUID getUuid();

        String getTitle();

        String getThumbnail();

        String getCategory();

        String getSchool();

        Integer getPageCount();

        Integer getDocumentYear();

        Long getLikeCount();

        Long getCommentCount();
    }
}
