package com.studydocs.modules.review.event.producer;

import com.studydocs.config.KafkaTopicConfig;
import com.studydocs.modules.review.event.publisher.ReviewEventPublisher;
import com.studydocs.shared.event.ReviewEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReviewEventProducer implements ReviewEventPublisher {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Override
    public void publishReviewEvent(String documentId, String userId, boolean isAdd) {
        ReviewEvent event = ReviewEvent.builder()
                .documentId(documentId)
                .userId(userId)
                .isAdd(isAdd)
                .build();

        kafkaTemplate.send(KafkaTopicConfig.REVIEW_TOPIC, documentId, event)
                .whenComplete((result, ex) -> {
                    if (ex != null) {
                        log.error("[Kafka Producer] Lỗi khi publish review event cho documentId={}: {}",
                                documentId, ex.getMessage());
                    } else {
                        log.debug("[Kafka Producer] Đã publish review event: documentId={}, userId={}, isAdd={}",
                                documentId, userId, isAdd);
                    }
                });
    }
}
