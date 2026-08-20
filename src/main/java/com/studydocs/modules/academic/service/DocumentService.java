package com.studydocs.modules.academic.service;

import com.studydocs.modules.academic.dto.AcademicDtos;
import com.studydocs.modules.academic.dto.DocumentSummaryDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface DocumentService {
    List<DocumentSummaryDto> getMostLiked(int limit);
    List<DocumentSummaryDto> getNewest(int limit);
    DocumentSummaryDto getDocumentById(String id);
    List<DocumentSummaryDto> searchDocuments(String query);
    List<DocumentSummaryDto> getMyDocuments(String userId);
    Map<String, Long> getMyDocumentCount(String userId);
    void incrementDownloadCount(String documentId);
    AcademicDtos.DocumentInitiateResponse initiateDocumentUpload(AcademicDtos.InitiateDocumentUploadRequest request, String uploaderId);
    DocumentSummaryDto completeDocumentUpload(String documentId, AcademicDtos.CompleteDocumentUploadRequest request);
    DocumentSummaryDto uploadDocument(MultipartFile file, String title, String description, Long universityId, Long facultyId, Long subjectId, Boolean isPublic, String uploaderId);
    DocumentSummaryDto createDocument(AcademicDtos.CreateDocumentRequest request, String uploaderId);
}
