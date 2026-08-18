package com.studydocs.modules.academic.service.impl;

import com.studydocs.modules.academic.dto.DocumentSummaryDto;
import com.studydocs.modules.academic.entity.DocumentEntity;
import com.studydocs.modules.academic.repository.DocumentRepository;
import com.studydocs.modules.academic.service.DocumentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DocumentServiceImpl implements DocumentService {

    private final DocumentRepository documentRepository;

    @Override
    public List<DocumentSummaryDto> getMostLiked(int limit) {
        return documentRepository.findTop10ByIsPublicTrueOrderByLikeCountDesc().stream()
                .map(this::toSummaryDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<DocumentSummaryDto> getNewest(int limit) {
        return documentRepository.findTop10ByIsPublicTrueOrderByCreatedAtDesc().stream()
                .map(this::toSummaryDto)
                .collect(Collectors.toList());
    }

    @Override
    public DocumentSummaryDto getDocumentById(String id) {
        DocumentEntity doc = documentRepository.findById(id)
                .orElseThrow(() -> new com.studydocs.shared.exception.AppException(com.studydocs.shared.exception.ErrorCode.DOCUMENT_NOT_FOUND));
        
        doc.setViewCount((doc.getViewCount() != null ? doc.getViewCount() : 0) + 1);
        documentRepository.save(doc);
        return toSummaryDto(doc);
    }

    @Override
    public List<DocumentSummaryDto> searchDocuments(String query) {
        return documentRepository.searchDocuments(query).stream()
                .map(this::toSummaryDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<DocumentSummaryDto> getMyDocuments(String userId) {
        return documentRepository.findByUploaderId(userId).stream()
                .map(this::toSummaryDto)
                .collect(Collectors.toList());
    }

    @Override
    public Map<String, Long> getMyDocumentCount(String userId) {
        long count = documentRepository.findByUploaderId(userId).size();
        return Map.of("count", count);
    }

    @Override
    public void incrementDownloadCount(String documentId) {
        Optional<DocumentEntity> docOpt = documentRepository.findById(documentId);
        if (docOpt.isPresent()) {
            DocumentEntity doc = docOpt.get();
            doc.setDownloadCount(doc.getDownloadCount() + 1);
            documentRepository.save(doc);
        }
    }

    private DocumentSummaryDto toSummaryDto(DocumentEntity doc) {
        String schoolName = doc.getUniversityId() != null ? "Đại học Bách Khoa TP.HCM" : "Đại học Quốc Gia";
        String thumbnail = doc.getFileUrl() != null ? doc.getFileUrl() : "https://via.placeholder.com/150";

        return DocumentSummaryDto.builder()
                .id(doc.getId())
                .title(doc.getTitle())
                .description(doc.getDescription())
                .fileUrl(doc.getFileUrl())
                .fileSize(doc.getFileSize())
                .fileType(doc.getFileType())
                .uploaderId(doc.getUploaderId())
                .uploaderName("Admin User")
                .thumbnail(thumbnail)
                .category("Công nghệ thông tin")
                .school(schoolName)
                .pageCount(15)
                .year("2024")
                .universityId(doc.getUniversityId())
                .universityName(schoolName)
                .facultyId(doc.getFacultyId())
                .subjectId(doc.getSubjectId())
                .likeCount(doc.getLikeCount() != null ? doc.getLikeCount() : 0)
                .commentCount(5)
                .downloadCount(doc.getDownloadCount() != null ? doc.getDownloadCount() : 0)
                .viewCount(doc.getViewCount() != null ? doc.getViewCount() : 0)
                .isLiked(false)
                .isBookmarked(false)
                .isPublic(doc.getIsPublic())
                .createdAt(doc.getCreatedAt())
                .build();
    }
}
