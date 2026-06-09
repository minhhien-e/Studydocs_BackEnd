package com.example.educationservice.document.service;

import com.example.educationservice.document.dto.request.CreateDocumentRequest;
import com.example.educationservice.document.dto.request.MediaCallbackRequest;
import com.example.educationservice.document.dto.request.UpdateDocumentDisplayMetadataRequest;
import com.example.educationservice.document.dto.request.UpdateDocumentStatsRequest;
import com.example.educationservice.document.dto.response.CreateDocumentResponse;
import com.example.educationservice.document.dto.response.DocumentListResponse;
import com.example.educationservice.document.dto.response.DocumentResponse;

import java.util.UUID;

public interface DocumentService {

    CreateDocumentResponse createDocument(CreateDocumentRequest request);

    DocumentListResponse getDocuments(int page, int pageSize);

    DocumentResponse getDocumentByUuid(UUID uuid);

    DocumentResponse updateDocumentStats(UUID uuid, UpdateDocumentStatsRequest request);

    DocumentResponse updateDocumentDisplayMetadata(UUID uuid, UpdateDocumentDisplayMetadataRequest request);

    void handleMediaCallback(MediaCallbackRequest request);
}
