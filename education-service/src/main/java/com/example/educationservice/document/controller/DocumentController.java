package com.example.educationservice.document.controller;

import com.example.educationservice.common.response.ApiResponse;
import com.example.educationservice.document.dto.request.CreateDocumentRequest;
import com.example.educationservice.document.dto.request.MediaCallbackRequest;
import com.example.educationservice.document.dto.request.UpdateDocumentDisplayMetadataRequest;
import com.example.educationservice.document.dto.request.UpdateDocumentStatsRequest;
import com.example.educationservice.document.dto.response.CreateDocumentResponse;
import com.example.educationservice.document.dto.response.DocumentListResponse;
import com.example.educationservice.document.dto.response.DocumentResponse;
import com.example.educationservice.document.service.DocumentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/documents")
@RequiredArgsConstructor
public class DocumentController {

    private final DocumentService documentService;

    @PostMapping
    public ResponseEntity<ApiResponse<CreateDocumentResponse>> createDocument(
            @Valid @RequestBody CreateDocumentRequest request
    ) {
        CreateDocumentResponse response = documentService.createDocument(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(response));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<DocumentListResponse>> getDocuments(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "5") int pageSize
    ) {
        DocumentListResponse response = documentService.getDocuments(page, pageSize);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<ApiResponse<DocumentResponse>> getDocument(@PathVariable String uuid) {
        DocumentResponse response = documentService.getDocumentByUuid(UUID.fromString(uuid));
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PutMapping("/{uuid}/stats")
    public ResponseEntity<ApiResponse<DocumentResponse>> updateDocumentStats(
            @PathVariable String uuid,
            @Valid @RequestBody UpdateDocumentStatsRequest request
    ) {
        DocumentResponse response = documentService.updateDocumentStats(UUID.fromString(uuid), request);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PatchMapping("/{uuid}/display-metadata")
    public ResponseEntity<ApiResponse<DocumentResponse>> updateDocumentDisplayMetadata(
            @PathVariable String uuid,
            @Valid @RequestBody UpdateDocumentDisplayMetadataRequest request
    ) {
        DocumentResponse response = documentService.updateDocumentDisplayMetadata(UUID.fromString(uuid), request);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PostMapping("/callback")
    public ResponseEntity<ApiResponse<Void>> handleMediaCallback(
            @Valid @RequestBody MediaCallbackRequest request
    ) {
        documentService.handleMediaCallback(request);
        return ResponseEntity.ok(ApiResponse.success(null));
    }
}
