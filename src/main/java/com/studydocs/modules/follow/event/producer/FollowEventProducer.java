package com.studydocs.modules.follow.event.producer;

import com.studydocs.config.KafkaTopicConfig;
import com.studydocs.modules.follow.event.publisher.FollowEventPublisher;
import com.studydocs.shared.event.UserFollowEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class FollowEventProducer implements FollowEventPublisher {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Override
    public void publishFollowEvent(String followerId, String followingId, boolean isAdd) {
        UserFollowEvent event = UserFollowEvent.builder()
                .followerId(followerId)
                .followingId(followingId)
                .isAdd(isAdd)
                .build();

        kafkaTemplate.send(KafkaTopicConfig.USER_FOLLOW_TOPIC, followingId, event)
                .whenComplete((result, ex) -> {
                    if (ex != null) {
                        log.error("[Kafka Producer] Lỗi khi publish follow event cho followingId={}: {}",
                                followingId, ex.getMessage());
                    } else {
                        log.debug("[Kafka Producer] Đã publish follow event: followerId={}, followingId={}, isAdd={}",
                                followerId, followingId, isAdd);
                    }
                });
    }
}
