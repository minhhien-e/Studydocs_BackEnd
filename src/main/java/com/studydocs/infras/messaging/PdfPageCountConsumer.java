package com.studydocs.infras.messaging;

import com.studydocs.config.KafkaTopicConfig;
import com.studydocs.shared.event.DocumentUploadedEvent;
import com.studydocs.modules.academic.repository.DocumentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

/**
 * Kafka Consumer: lắng nghe event đếm trang, tải PDF từ URL và cập nhật pageCount vào DB.
 * Toàn bộ quá trình này diễn ra bất đồng bộ, không ảnh hưởng đến luồng upload.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PdfPageCountConsumer {

    private final DocumentRepository documentRepository;

    private static final HttpClient HTTP_CLIENT = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(15))
            .build();

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

            int pageCount = countPdfPages(fileUrl);

            documentRepository.findById(documentId).ifPresentOrElse(doc -> {
                doc.setPageCount(pageCount);
                documentRepository.save(doc);
                log.info("[Kafka Consumer] Cập nhật pageCount={} cho documentId={}", pageCount, documentId);
            }, () -> log.warn("[Kafka Consumer] Không tìm thấy document với id={}", documentId));

        } catch (Exception e) {
            log.error("[Kafka Consumer] Lỗi khi đếm số trang cho documentId={}: {}", documentId, e.getMessage(), e);
        }
    }

    /**
     * Tải PDF từ URL và đếm số trang bằng Apache PDFBox.
     *
     * @param pdfUrl URL đầy đủ của file PDF
     * @return số trang của file PDF
     * @throws Exception nếu không tải được hoặc file không hợp lệ
     */
    private int countPdfPages(String pdfUrl) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(pdfUrl))
                .timeout(Duration.ofSeconds(30))
                .GET()
                .build();

        HttpResponse<InputStream> response = HTTP_CLIENT.send(request, HttpResponse.BodyHandlers.ofInputStream());

        if (response.statusCode() != 200) {
            throw new RuntimeException("HTTP " + response.statusCode() + " khi tải PDF từ: " + pdfUrl);
        }

        try (InputStream is = new BufferedInputStream(response.body());
             PDDocument document = Loader.loadPDF(is.readAllBytes())) {
            return document.getNumberOfPages();
        }
    }
}
