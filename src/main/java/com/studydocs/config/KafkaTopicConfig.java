package com.studydocs.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {

    public static final String DOCUMENT_PAGE_COUNT_TOPIC = "document-page-count-topic";
    public static final String DOCUMENT_INTERACTED_TOPIC = "document-interacted-topic";
    public static final String REVIEW_TOPIC = "review-topic";
    public static final String USER_FOLLOW_TOPIC = "user-follow-topic";
    public static final String NOTIFICATION_TOPIC = "notification-topic";

    @Bean
    public NewTopic documentPageCountTopic() {
        return TopicBuilder.name(DOCUMENT_PAGE_COUNT_TOPIC)
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic notificationTopic() {
        return TopicBuilder.name(NOTIFICATION_TOPIC)
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic documentInteractedTopic() {
        return TopicBuilder.name(DOCUMENT_INTERACTED_TOPIC)
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic reviewTopic() {
        return TopicBuilder.name(REVIEW_TOPIC)
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic userFollowTopic() {
        return TopicBuilder.name(USER_FOLLOW_TOPIC)
                .partitions(3)
                .replicas(1)
                .build();
    }
}
