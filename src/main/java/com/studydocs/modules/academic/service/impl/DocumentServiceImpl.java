package com.studydocs.modules.academic.service.impl;

import com.studydocs.infras.storage.FileStorageService;
import com.studydocs.modules.academic.dto.AcademicDtos;
import com.studydocs.modules.academic.dto.DocumentSummaryDto;
import com.studydocs.modules.academic.entity.DocumentEntity;
import com.studydocs.modules.academic.entity.DocumentStatus;
import com.studydocs.modules.academic.repository.DocumentRepository;
import com.studydocs.modules.academic.repository.SubjectRepository;
import com.studydocs.modules.academic.repository.UniversityRepository;
import com.studydocs.modules.academic.service.DocumentService;
import com.studydocs.modules.user.repository.UserRepository;
import com.studydocs.modules.user.entity.UserEntity;
import com.studydocs.modules.academic.entity.UniversityEntity;
import com.studydocs.modules.academic.entity.SubjectEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DocumentServiceImpl implements DocumentService {

    private final DocumentRepository documentRepository;
    private final FileStorageService fileStorageService;
    private final UserRepository userRepository;
    private final UniversityRepository universityRepository;
    private final SubjectRepository subjectRepository;

    @Override
    public List<DocumentSummaryDto> getMostLiked(int limit) {
        return documentRepository.findTop10ByIsPublicTrueAndStatusOrderByLikeCountDesc(DocumentStatus.COMPLETED).stream()
                .map(this::toSummaryDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<DocumentSummaryDto> getNewest(int limit) {
        return documentRepository.findTop10ByIsPublicTrueAndStatusOrderByCreatedAtDesc(DocumentStatus.COMPLETED).stream()
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

    @Override
    public AcademicDtos.DocumentInitiateResponse initiateDocumentUpload(AcademicDtos.InitiateDocumentUploadRequest request, String uploaderId) {
        String mediaId = UUID.randomUUID().toString();
        String uploadUrl = "/api/v1/media/" + mediaId + "/complete-upload";

        DocumentEntity entity = DocumentEntity.builder()
                .title(request.getTitle() != null ? request.getTitle() : "Untitled Document")
                .description(request.getDescription())
                .fileSize(request.getFileSize())
                .fileType(request.getFileType() != null ? request.getFileType() : "application/pdf")
                .uploaderId(uploaderId != null ? uploaderId : "anonymous")
                .universityId(request.getUniversityId())
                .facultyId(request.getFacultyId())
                .departmentId(request.getDepartmentId())
                .subjectId(request.getSubjectId())
                .schoolYear(request.getSchoolYear())
                .isPublic(request.getIsPublic() != null ? request.getIsPublic() : true)
                .status(DocumentStatus.PENDING)
                .likeCount(0)
                .downloadCount(0)
                .viewCount(0)
                .build();

        DocumentEntity saved = documentRepository.save(entity);

        return AcademicDtos.DocumentInitiateResponse.builder()
                .documentId(saved.getId())
                .mediaId(mediaId)
                .uploadUrl(uploadUrl)
                .status(DocumentStatus.PENDING.name())
                .build();
    }

    @Override
    public DocumentSummaryDto completeDocumentUpload(String documentId, AcademicDtos.CompleteDocumentUploadRequest request) {
        DocumentEntity doc = documentRepository.findById(documentId)
                .orElseThrow(() -> new com.studydocs.shared.exception.AppException(com.studydocs.shared.exception.ErrorCode.DOCUMENT_NOT_FOUND));

        String resolvedFileUrl = request.getFileUrl();
        if (resolvedFileUrl == null || resolvedFileUrl.trim().isEmpty()) {
            String mediaId = request.getMediaId() != null ? request.getMediaId() : UUID.randomUUID().toString();
            resolvedFileUrl = "/api/v1/media/files/" + mediaId + ".pdf";
        }

        doc.setFileUrl(resolvedFileUrl);
        if (request.getFileSize() != null) {
            doc.setFileSize(request.getFileSize());
        }
        if (request.getFileType() != null) {
            doc.setFileType(request.getFileType());
        }
        doc.setStatus(DocumentStatus.COMPLETED);

        DocumentEntity saved = documentRepository.save(doc);
        return toSummaryDto(saved);
    }

    @Override
    public DocumentSummaryDto uploadDocument(MultipartFile file, String title, String description, Long universityId, Long facultyId, Long departmentId, Long subjectId, String schoolYear, Boolean isPublic, String uploaderId) {
        String storedFileName = fileStorageService.storeFile(file);
        String fileUrl = "/api/v1/media/files/" + storedFileName;
        Long fileSize = file != null ? file.getSize() : 0L;
        String fileType = file != null ? file.getContentType() : "application/pdf";

        String docTitle = (title != null && !title.trim().isEmpty()) 
                ? title 
                : (file != null ? file.getOriginalFilename() : "Untitled Document");

        DocumentEntity entity = DocumentEntity.builder()
                .title(docTitle)
                .description(description)
                .fileUrl(fileUrl)
                .fileSize(fileSize)
                .fileType(fileType)
                .uploaderId(uploaderId != null ? uploaderId : "anonymous")
                .universityId(universityId)
                .facultyId(facultyId)
                .departmentId(departmentId)
                .subjectId(subjectId)
                .schoolYear(schoolYear)
                .isPublic(isPublic != null ? isPublic : true)
                .status(DocumentStatus.COMPLETED)
                .likeCount(0)
                .downloadCount(0)
                .viewCount(0)
                .build();

        DocumentEntity saved = documentRepository.save(entity);
        return toSummaryDto(saved);
    }

    @Override
    public DocumentSummaryDto createDocument(AcademicDtos.CreateDocumentRequest request, String uploaderId) {
        DocumentEntity entity = DocumentEntity.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .fileUrl(request.getFileUrl())
                .fileSize(request.getFileSize())
                .fileType(request.getFileType())
                .uploaderId(uploaderId != null ? uploaderId : "anonymous")
                .universityId(request.getUniversityId())
                .facultyId(request.getFacultyId())
                .departmentId(request.getDepartmentId())
                .subjectId(request.getSubjectId())
                .schoolYear(request.getSchoolYear())
                .isPublic(request.getIsPublic() != null ? request.getIsPublic() : true)
                .status(DocumentStatus.COMPLETED)
                .likeCount(0)
                .downloadCount(0)
                .viewCount(0)
                .build();

        DocumentEntity saved = documentRepository.save(entity);
        return toSummaryDto(saved);
    }

    private DocumentSummaryDto toSummaryDto(DocumentEntity doc) {
        String schoolName = "Đại học Quốc Gia";
        if (doc.getUniversityId() != null) {
            schoolName = universityRepository.findById(doc.getUniversityId())
                    .map(UniversityEntity::getName)
                    .orElse("Đại học Quốc Gia");
        }

        String uploaderName = "Admin User";
        if (doc.getUploaderId() != null) {
            uploaderName = userRepository.findById(doc.getUploaderId())
                    .map(UserEntity::getFullName)
                    .orElse("Admin User");
        }

        String category = "Chung";
        if (doc.getSubjectId() != null) {
            category = subjectRepository.findById(doc.getSubjectId())
                    .map(SubjectEntity::getName)
                    .orElse("Chung");
        }

        String thumbnail = doc.getFileUrl() != null ? doc.getFileUrl() : "https://via.placeholder.com/150";
        String statusStr = doc.getStatus() != null ? doc.getStatus().name() : DocumentStatus.COMPLETED.name();

        return DocumentSummaryDto.builder()
                .id(doc.getId())
                .title(doc.getTitle())
                .description(doc.getDescription())
                .fileUrl(doc.getFileUrl())
                .fileSize(doc.getFileSize())
                .fileType(doc.getFileType())
                .uploaderId(doc.getUploaderId())
                .uploaderName(uploaderName)
                .thumbnail(thumbnail)
                .category(category)
                .school(schoolName)
                .pageCount(15) // Keep hardcoded as it requires PDF parsing
                .year(doc.getSchoolYear() != null ? doc.getSchoolYear() : "2024")
                .universityId(doc.getUniversityId())
                .universityName(schoolName)
                .facultyId(doc.getFacultyId())
                .departmentId(doc.getDepartmentId())
                .subjectId(doc.getSubjectId())
                .likeCount(doc.getLikeCount() != null ? doc.getLikeCount() : 0)
                .commentCount(0) // Keep hardcoded for now
                .downloadCount(doc.getDownloadCount() != null ? doc.getDownloadCount() : 0)
                .viewCount(doc.getViewCount() != null ? doc.getViewCount() : 0)
                .isLiked(false)
                .isBookmarked(false)
                .isPublic(doc.getIsPublic())
                .status(statusStr)
                .createdAt(doc.getCreatedAt())
                .build();
    }
}
