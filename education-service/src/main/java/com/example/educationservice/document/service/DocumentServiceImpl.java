package com.example.educationservice.document.service;

import com.example.educationservice.academic.repository.UniversityRepository;
import com.example.educationservice.document.dto.request.CreateDocumentRequest;
import com.example.educationservice.document.dto.request.MediaCallbackRequest;
import com.example.educationservice.document.dto.request.UpdateDocumentDisplayMetadataRequest;
import com.example.educationservice.document.dto.request.UpdateDocumentStatsRequest;
import com.example.educationservice.document.dto.response.CreateDocumentResponse;
import com.example.educationservice.document.dto.response.DocumentListItemResponse;
import com.example.educationservice.document.dto.response.DocumentListResponse;
import com.example.educationservice.document.dto.response.DocumentResponse;
import com.example.educationservice.document.dto.response.UploadInfo;
import com.example.educationservice.document.entity.DocumentEntity;
import com.example.educationservice.document.entity.DocumentStatus;
import com.example.educationservice.document.mapper.DocumentMapper;
import com.example.educationservice.document.repository.DocumentRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DocumentServiceImpl implements DocumentService {

    private final DocumentRepository documentRepository;
    private final UniversityRepository universityRepository;
    private final DocumentMapper documentMapper;

    // TODO: move to application.yml when media service URL is finalized
    private static final String MEDIA_UPLOAD_URL_PLACEHOLDER = "http://media-service/api/v1/upload";

    @Override
    @Transactional
    public CreateDocumentResponse createDocument(CreateDocumentRequest request) {
        if (!universityRepository.existsById(request.universityId())) {
            throw new RuntimeException("University not found with id: " + request.universityId());
        }

        DocumentEntity entity = new DocumentEntity();
        entity.setUuid(UUID.randomUUID());
        entity.setUniversityId(request.universityId());
        entity.setSubjectId(request.subjectId());
        entity.setTitle(request.title().trim());
        entity.setYear(request.year());
        entity.setDescription(request.description());
        entity.setStatus(DocumentStatus.UPLOADING);
        entity.setMediaId(null);
        entity.setThumbnailUrl(null);
        entity.setPageCount(0);
        entity.setLikeCount(0L);
        entity.setCommentCount(0L);

        DocumentEntity saved = documentRepository.save(entity);

        DocumentResponse docResponse = documentMapper.toResponse(saved);
        UploadInfo uploadInfo = new UploadInfo(
                MEDIA_UPLOAD_URL_PLACEHOLDER,
                saved.getUuid().toString()
        );

        return new CreateDocumentResponse(docResponse, uploadInfo);
    }

    @Override
    public DocumentListResponse getDocuments(int page, int pageSize) {
        int normalizedPage = Math.max(page, 1);
        int normalizedPageSize = normalizePageSize(pageSize);

        Page<DocumentRepository.DocumentListProjection> result = documentRepository.findDocumentListByStatus(
                DocumentStatus.READY,
                PageRequest.of(normalizedPage - 1, normalizedPageSize)
        );

        List<DocumentListItemResponse> items = result.getContent().stream()
                .map(this::toListItemResponse)
                .toList();

        long total = result.getTotalElements();
        boolean hasMore = (long) normalizedPage * normalizedPageSize < total;

        return new DocumentListResponse(items, normalizedPage, normalizedPageSize, total, hasMore);
    }

    @Override
    public DocumentResponse getDocumentByUuid(UUID uuid) {
        DocumentEntity entity = documentRepository.findByUuid(uuid)
                .orElseThrow(() -> new RuntimeException("Document not found with uuid: " + uuid));

        return documentMapper.toResponse(entity);
    }

    @Override
    @Transactional
    public DocumentResponse updateDocumentStats(UUID uuid, UpdateDocumentStatsRequest request) {
        DocumentEntity entity = findDocumentByUuid(uuid);

        if (request.likeCount() != null) {
            entity.setLikeCount(request.likeCount());
        }
        if (request.commentCount() != null) {
            entity.setCommentCount(request.commentCount());
        }

        return documentMapper.toResponse(documentRepository.save(entity));
    }

    @Override
    @Transactional
    public DocumentResponse updateDocumentDisplayMetadata(UUID uuid, UpdateDocumentDisplayMetadataRequest request) {
        DocumentEntity entity = findDocumentByUuid(uuid);

        if (request.thumbnail() != null) {
            entity.setThumbnailUrl(request.thumbnail());
        }
        if (request.pageCount() != null) {
            entity.setPageCount(request.pageCount());
        }

        return documentMapper.toResponse(documentRepository.save(entity));
    }

    @Override
    @Transactional
    public void handleMediaCallback(MediaCallbackRequest request) {
        UUID docUuid = UUID.fromString(request.documentUuid());

        DocumentEntity entity = findDocumentByUuid(docUuid);

        entity.setMediaId(request.mediaId());
        entity.setStatus(DocumentStatus.READY);

        documentRepository.save(entity);
    }

    private DocumentEntity findDocumentByUuid(UUID uuid) {
        return documentRepository.findByUuid(uuid)
                .orElseThrow(() -> new RuntimeException("Document not found with uuid: " + uuid));
    }

    private int normalizePageSize(int pageSize) {
        if (pageSize < 1) {
            return 5;
        }
        return Math.min(pageSize, 50);
    }

    private DocumentListItemResponse toListItemResponse(DocumentRepository.DocumentListProjection projection) {
        return new DocumentListItemResponse(
                projection.getUuid().toString(),
                projection.getTitle(),
                projection.getThumbnail(),
                projection.getCategory(),
                projection.getSchool(),
                projection.getPageCount(),
                formatSchoolYear(projection.getDocumentYear()),
                projection.getLikeCount(),
                projection.getCommentCount(),
                false,
                false
        );
    }

    private String formatSchoolYear(Integer year) {
        if (year == null) {
            return null;
        }
        return year + "/" + (year + 1);
    }
}
