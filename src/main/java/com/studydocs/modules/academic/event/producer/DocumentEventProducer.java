package com.studydocs.modules.academic.event.producer;

import com.studydocs.config.KafkaTopicConfig;
import com.studydocs.shared.event.DocumentUploadedEvent;
import com.studydocs.modules.academic.event.publisher.DocumentEventPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.studydocs.shared.event.DocumentInteractedEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

/**
 * Kafka implementation của DocumentEventPublisher.
 * Chịu trách nhiệm serialize và gửi event lên Kafka topic.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DocumentEventProducer implements DocumentEventPublisher {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    /**
     * {@inheritDoc}
     * Publish event lên Kafka topic để Consumer xử lý bất đồng bộ.
     */
    @Override
    public void publishPageCountEvent(String documentId, String fileUrl, String uploaderId) {
        if (fileUrl == null || fileUrl.isBlank()) {
            log.warn("[Kafka Producer] Bỏ qua event đếm trang: documentId={} không có fileUrl", documentId);
            return;
        }

        DocumentUploadedEvent event = DocumentUploadedEvent.builder()
                .documentId(documentId)
                .fileUrl(fileUrl)
                .uploaderId(uploaderId)
                .build();

        kafkaTemplate.send(KafkaTopicConfig.DOCUMENT_PAGE_COUNT_TOPIC, documentId, event)
                .whenComplete((result, ex) -> {
                    if (ex != null) {
                        log.error("[Kafka Producer] Lỗi khi publish event đếm trang cho documentId={}: {}",
                                documentId, ex.getMessage());
                    } else {
                        log.info("[Kafka Producer] Đã publish event đếm trang: documentId={}, topic={}, offset={}",
                                documentId,
                                result.getRecordMetadata().topic(),
                                result.getRecordMetadata().offset());
                    }
                });
    }

    @Override
    public void publishInteractionEvent(String documentId, String userId, String type, boolean isAdd) {
        DocumentInteractedEvent event = DocumentInteractedEvent.builder()
                .documentId(documentId)
                .userId(userId)
                .type(type)
                .isAdd(isAdd)
                .build();

        kafkaTemplate.send(KafkaTopicConfig.DOCUMENT_INTERACTED_TOPIC, documentId, event)
                .whenComplete((result, ex) -> {
                    if (ex != null) {
                        log.error("[Kafka Producer] Lỗi khi publish interaction event cho documentId={}: {}",
                                documentId, ex.getMessage());
                    } else {
                        log.debug("[Kafka Producer] Đã publish interaction event: documentId={}, type={}, isAdd={}",
                                documentId, type, isAdd);
                    }
                });
    }
}
