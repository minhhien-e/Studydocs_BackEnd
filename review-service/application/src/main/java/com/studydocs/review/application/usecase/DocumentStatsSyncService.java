package com.studydocs.review.application.usecase;

import com.studydocs.review.domain.enums.InteractionType;
import com.studydocs.review.domain.enums.TargetType;
import com.studydocs.review.domain.repository.InteractionRepository;
import com.studydocs.review.domain.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
@Slf4j
public class DocumentStatsSyncService {

    private final ReviewRepository reviewRepository;
    private final InteractionRepository interactionRepository;

    @Value("${app.education-service.url:http://localhost:8088}")
    private String educationServiceUrl;

    private final HttpClient httpClient = HttpClient.newHttpClient();

    public void syncStats(UUID documentId) {
        CompletableFuture.runAsync(() -> {
            try {
                long totalReviews = reviewRepository.countByDocumentId(documentId);
                long totalLikes = interactionRepository.countByTargetIdAndTargetTypeAndInteractionType(
                        documentId, TargetType.DOCUMENT, InteractionType.LIKE);

                String json = String.format("{\"likeCount\":%d,\"commentCount\":%d}", totalLikes, totalReviews);
                
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(educationServiceUrl + "/api/v1/documents/" + documentId + "/stats"))
                        .header("Content-Type", "application/json")
                        .PUT(HttpRequest.BodyPublishers.ofString(json))
                        .build();

                log.info("Sending stats update to document service: {} for doc {}", json, documentId);
                HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
                if (response.statusCode() >= 300) {
                    log.error("Failed to sync stats to education-service: status {}, body {}", response.statusCode(), response.body());
                } else {
                    log.info("Successfully synced stats to education-service");
                }
            } catch (Exception e) {
                log.error("Error syncing stats to education-service for document {}", documentId, e);
            }
        });
    }
}
