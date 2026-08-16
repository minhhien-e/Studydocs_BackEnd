package com.studydocs.modules.academic.service;

import com.studydocs.modules.academic.dto.DocumentSummaryDto;

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
}
