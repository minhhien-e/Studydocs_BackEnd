package com.studydocs.modules.academic.event.consumer;

import com.studydocs.config.KafkaTopicConfig;
import com.studydocs.modules.academic.repository.DocumentRepository;
import com.studydocs.shared.event.DocumentInteractedEvent;
import com.studydocs.shared.event.ReviewEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class DocumentStatsConsumer {

    private final DocumentRepository documentRepository;

    @KafkaListener(
            topics = KafkaTopicConfig.DOCUMENT_INTERACTED_TOPIC,
            groupId = "document-stats-group",
            containerFactory = "kafkaListenerContainerFactory"
    )
    @Transactional
    public void handleDocumentInteracted(DocumentInteractedEvent event) {
        if (!"LIKE".equalsIgnoreCase(event.getType())) return;
        
        String documentId = event.getDocumentId();
        
        documentRepository.findById(documentId).ifPresent(doc -> {
            int current = doc.getLikeCount() != null ? doc.getLikeCount() : 0;
            if (event.isAdd()) {
                doc.setLikeCount(current + 1);
            } else {
                doc.setLikeCount(Math.max(0, current - 1));
            }
            documentRepository.save(doc);
            log.debug("[DocumentStats] Cập nhật likeCount cho document {}", documentId);
        });
    }

    @KafkaListener(
            topics = KafkaTopicConfig.REVIEW_TOPIC,
            groupId = "document-stats-group",
            containerFactory = "kafkaListenerContainerFactory"
    )
    @Transactional
    public void handleReviewEvent(ReviewEvent event) {
        String documentId = event.getDocumentId();

        documentRepository.findById(documentId).ifPresent(doc -> {
            int current = doc.getCommentCount() != null ? doc.getCommentCount() : 0;
            if (event.isAdd()) {
                doc.setCommentCount(current + 1);
            } else {
                doc.setCommentCount(Math.max(0, current - 1));
            }
            documentRepository.save(doc);
            log.debug("[DocumentStats] Cập nhật commentCount cho document {}", documentId);
        });
    }
}
