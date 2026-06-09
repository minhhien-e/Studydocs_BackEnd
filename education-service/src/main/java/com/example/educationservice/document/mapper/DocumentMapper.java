package com.example.educationservice.document.mapper;

import com.example.educationservice.document.dto.response.DocumentResponse;
import com.example.educationservice.document.entity.DocumentEntity;
import org.springframework.stereotype.Component;

@Component
public class DocumentMapper {

    public DocumentResponse toResponse(DocumentEntity entity) {
        return new DocumentResponse(
                entity.getUuid().toString(),
                entity.getTitle(),
                entity.getYear(),
                entity.getDescription(),
                entity.getStatus().name(),
                entity.getMediaId(),
                entity.getThumbnailUrl(),
                entity.getPageCount(),
                entity.getLikeCount(),
                entity.getCommentCount(),
                entity.getUniversityId(),
                entity.getSubjectId(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}
