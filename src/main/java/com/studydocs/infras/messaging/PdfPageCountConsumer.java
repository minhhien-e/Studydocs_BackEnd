package com.studydocs.infras.messaging;

import com.studydocs.config.KafkaTopicConfig;
import com.studydocs.modules.system.service.MediaService;
import com.studydocs.shared.event.DocumentUploadedEvent;
import com.studydocs.modules.academic.repository.DocumentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

/**
 * Kafka Consumer: lắng nghe event đếm trang, lấy pageCount qua Cloudinary API và cập nhật DB.
 * Toàn bộ quá trình này diễn ra bất đồng bộ, không ảnh hưởng đến luồng upload.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PdfPageCountConsumer {

    private final DocumentRepository documentRepository;
    private final MediaService mediaService;

    @KafkaListener(
            topics = KafkaTopicConfig.DOCUMENT_PAGE_COUNT_TOPIC,
            groupId = "studydocs-page-count-group",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void handlePageCountEvent(DocumentUploadedEvent event) {
        String documentId = event.getDocumentId();
        String fileUrl = event.getFileUrl();

        log.info("[Kafka Consumer] Nhận event đếm trang: documentId={}, fileUrl={}", documentId, fileUrl);

        try {
            // Bỏ qua nếu URL là đường dẫn nội bộ (không có HTTP scheme)
            if (!fileUrl.startsWith("http://") && !fileUrl.startsWith("https://")) {
                log.warn("[Kafka Consumer] fileUrl không phải HTTP URL, bỏ qua: {}", fileUrl);
                return;
            }

            documentRepository.findById(documentId).ifPresentOrElse(doc -> {
                if (doc.getPageCount() != null && doc.getPageCount() > 0) {
                    log.info("[Kafka Consumer] pageCount đã có sẵn ({}) cho documentId={}, bỏ qua.", doc.getPageCount(), documentId);
                    return;
                }
                
                int pageCount = mediaService.getPdfPageCount(fileUrl);
                
                if (pageCount > 0) {
                    doc.setPageCount(pageCount);
                    documentRepository.save(doc);
                    log.info("[Kafka Consumer] Cập nhật pageCount={} cho documentId={}", pageCount, documentId);
                } else {
                    log.warn("[Kafka Consumer] Không lấy được pageCount cho documentId={}", documentId);
                }
            }, () -> log.warn("[Kafka Consumer] Không tìm thấy document với id={}", documentId));

        } catch (Exception e) {
            log.error("[Kafka Consumer] Lỗi khi đếm số trang cho documentId={}: {}", documentId, e.getMessage(), e);
        }
    }
}
