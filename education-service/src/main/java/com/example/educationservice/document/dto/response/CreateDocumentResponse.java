package com.example.educationservice.document.dto.response;

public record CreateDocumentResponse(
        DocumentResponse document,
        UploadInfo uploadInfo
) {
}
