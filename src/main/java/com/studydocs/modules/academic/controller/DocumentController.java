package com.studydocs.modules.academic.controller;

import com.studydocs.modules.academic.dto.AcademicDtos;
import com.studydocs.modules.academic.dto.DocumentSummaryDto;
import com.studydocs.modules.academic.service.DocumentService;
import com.studydocs.shared.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * Controller xử lý tài liệu học tập (tìm kiếm, công khai, cá nhân, bookmark, download, tương tác, upload).
 *
 * @author StudyDocs Team
 * @since 1.0.0
 */
@RestController
@RequestMapping("/education/documents")
@RequiredArgsConstructor
public class DocumentController {

    private final DocumentService documentService;

    @PostMapping("/initiate")
    public ApiResponse<AcademicDtos.DocumentInitiateResponse> initiateDocumentUpload(
            @RequestBody AcademicDtos.InitiateDocumentUploadRequest request,
            Authentication authentication) {
        String uploaderId = authentication != null ? authentication.getName() : "anonymous";
        return ApiResponse.success(documentService.initiateDocumentUpload(request, uploaderId));
    }

    @PostMapping("/{documentId}/complete-upload")
    public ApiResponse<DocumentSummaryDto> completeDocumentUpload(
            @PathVariable String documentId,
            @RequestBody(required = false) AcademicDtos.CompleteDocumentUploadRequest request) {
        if (request == null) {
            request = new AcademicDtos.CompleteDocumentUploadRequest();
        }
        return ApiResponse.success(documentService.completeDocumentUpload(documentId, request));
    }

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<DocumentSummaryDto> uploadDocument(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam(value = "universityId", required = false) Long universityId,
            @RequestParam(value = "facultyId", required = false) Long facultyId,
            @RequestParam(value = "departmentId", required = false) Long departmentId,
            @RequestParam(value = "subjectId", required = false) Long subjectId,
            @RequestParam(value = "schoolYear", required = false) String schoolYear,
            @RequestParam(value = "school_year", required = false) String schoolYearSnake,
            @RequestParam(value = "year", required = false) String year,
            @RequestParam(value = "isPublic", required = false, defaultValue = "true") Boolean isPublic,
            Authentication authentication) {
        String uploaderId = authentication != null ? authentication.getName() : "anonymous";
        String finalSchoolYear = schoolYear != null ? schoolYear : (schoolYearSnake != null ? schoolYearSnake : year);
        return ApiResponse.success(documentService.uploadDocument(file, title, description, universityId, facultyId, departmentId, subjectId, finalSchoolYear, isPublic, uploaderId));
    }

    @PostMapping
    public ApiResponse<DocumentSummaryDto> createDocument(@RequestBody AcademicDtos.CreateDocumentRequest request, Authentication authentication) {
        String uploaderId = authentication != null ? authentication.getName() : "anonymous";
        return ApiResponse.success(documentService.createDocument(request, uploaderId));
    }

    @GetMapping
    public ApiResponse<List<DocumentSummaryDto>> getAllDocuments(@RequestParam(value = "q", required = false) String query) {
        return ApiResponse.success(documentService.searchDocuments(query));
    }

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

    @GetMapping("/user/{userId}")
    public ApiResponse<List<DocumentSummaryDto>> getDocumentsByUser(@PathVariable String userId) {
        return ApiResponse.success(documentService.getMyDocuments(userId));
    }

    @GetMapping("/user/me/newest")
    public ApiResponse<List<DocumentSummaryDto>> getMyNewestDocuments(Authentication authentication) {
        String userId = authentication != null ? authentication.getName() : "anonymous";
        return ApiResponse.success(documentService.getMyDocuments(userId));
    }

    @GetMapping("/user/me/history")
    public ApiResponse<List<DocumentSummaryDto>> getMyHistoryDocuments(Authentication authentication) {
        // Mock history with newest documents since history tracking is not fully implemented
        return ApiResponse.success(documentService.getNewest(10));
    }

    @GetMapping("/user/me/saved")
    public ApiResponse<List<DocumentSummaryDto>> getMySavedDocuments(Authentication authentication) {
        String userId = authentication != null ? authentication.getName() : "anonymous";
        return ApiResponse.success(documentService.getMyBookmarkedDocuments(userId));
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
    public ApiResponse<String> bookmarkDocument(@PathVariable String documentId, Authentication authentication) {
        String userId = authentication != null ? authentication.getName() : "anonymous";
        documentService.handleInteraction(documentId, "BOOKMARK", userId);
        return ApiResponse.success("Document bookmarked");
    }

    @PostMapping("/{documentId}/download")
    public ApiResponse<String> downloadDocument(@PathVariable String documentId) {
        documentService.incrementDownloadCount(documentId);
        return ApiResponse.success("Download started");
    }

    @PostMapping("/{documentId}/interactions")
    public ApiResponse<Map<String, Object>> interactWithDocument(@PathVariable String documentId, @RequestBody Map<String, Object> body, Authentication authentication) {
        String type = (String) body.getOrDefault("type", "LIKE");
        String userId = authentication != null ? authentication.getName() : "anonymous";
        documentService.handleInteraction(documentId, type, userId);
        return ApiResponse.success(Map.of(
                "documentId", documentId,
                "type", type,
                "status", "success"
        ));
    }
}
