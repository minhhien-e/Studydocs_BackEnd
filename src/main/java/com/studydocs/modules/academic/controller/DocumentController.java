package com.studydocs.modules.academic.controller;

import com.studydocs.modules.academic.dto.DocumentSummaryDto;
import com.studydocs.modules.academic.service.DocumentService;
import com.studydocs.shared.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Controller xử lý tài liệu học tập (tìm kiếm, công khai, cá nhân, bookmark, download).
 *
 * @author StudyDocs Team
 * @since 1.0.0
 */
@RestController
@RequestMapping("/education/documents")
@RequiredArgsConstructor
public class DocumentController {

    private final DocumentService documentService;

    @GetMapping("/public/most-liked")
    public ApiResponse<List<DocumentSummaryDto>> getMostLiked(@RequestParam(value = "limit", defaultValue = "10") int limit) {
        return ApiResponse.success(documentService.getMostLiked(limit));
    }

    @GetMapping("/public/newest")
    public ApiResponse<List<DocumentSummaryDto>> getNewest(@RequestParam(value = "limit", defaultValue = "10") int limit) {
        return ApiResponse.success(documentService.getNewest(limit));
    }

    @GetMapping("/public/{id}")
    public ApiResponse<DocumentSummaryDto> getDocumentById(@PathVariable String id) {
        return ApiResponse.success(documentService.getDocumentById(id));
    }

    @GetMapping("/search")
    public ApiResponse<List<DocumentSummaryDto>> searchDocuments(@RequestParam(value = "q", required = false) String query) {
        return ApiResponse.success(documentService.searchDocuments(query));
    }

    @GetMapping("/user/me")
    public ApiResponse<List<DocumentSummaryDto>> getMyDocuments(Authentication authentication) {
        String userId = authentication != null ? authentication.getName() : "anonymous";
        return ApiResponse.success(documentService.getMyDocuments(userId));
    }

    @GetMapping("/user/me/newest")
    public ApiResponse<List<DocumentSummaryDto>> getMyNewestDocuments(Authentication authentication) {
        String userId = authentication != null ? authentication.getName() : "anonymous";
        return ApiResponse.success(documentService.getMyDocuments(userId));
    }

    @GetMapping("/user/me/history")
    public ApiResponse<List<DocumentSummaryDto>> getMyHistoryDocuments(Authentication authentication) {
        String userId = authentication != null ? authentication.getName() : "anonymous";
        return ApiResponse.success(documentService.getMyDocuments(userId));
    }

    @GetMapping("/user/me/count")
    public ApiResponse<Map<String, Long>> getMyDocumentCount(Authentication authentication) {
        String userId = authentication != null ? authentication.getName() : "anonymous";
        return ApiResponse.success(documentService.getMyDocumentCount(userId));
    }

    @GetMapping("/admin/stats/documents/total")
    public ApiResponse<Map<String, Object>> getAdminStatsTotalDocuments() {
        return ApiResponse.success(Map.of("totalDocuments", 120, "activeDocuments", 115));
    }

    @GetMapping("/admin/stats/system")
    public ApiResponse<Map<String, Object>> getAdminStatsSystem() {
        return ApiResponse.success(Map.of("totalUsers", 450, "totalDownloads", 3200, "totalStorageMb", 4500));
    }

    @PostMapping("/{documentId}/bookmark")
    public ApiResponse<String> bookmarkDocument(@PathVariable String documentId) {
        return ApiResponse.success("Document bookmarked");
    }

    @PostMapping("/{documentId}/download")
    public ApiResponse<String> downloadDocument(@PathVariable String documentId) {
        documentService.incrementDownloadCount(documentId);
        return ApiResponse.success("Download started");
    }
}
