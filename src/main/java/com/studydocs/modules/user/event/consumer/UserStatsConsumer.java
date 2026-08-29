package com.studydocs.modules.user.event.consumer;

import com.studydocs.config.KafkaTopicConfig;
import com.studydocs.modules.user.repository.UserRepository;
import com.studydocs.shared.event.DocumentInteractedEvent;
import com.studydocs.shared.event.DocumentUploadedEvent;
import com.studydocs.shared.event.ReviewEvent;
import com.studydocs.shared.event.UserFollowEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserStatsConsumer {

    private final UserRepository userRepository;

    @KafkaListener(
            topics = KafkaTopicConfig.DOCUMENT_PAGE_COUNT_TOPIC, // Dùng chung topic upload
            groupId = "user-stats-group",
            containerFactory = "kafkaListenerContainerFactory"
    )
    @Transactional
    public void handleDocumentUploaded(DocumentUploadedEvent event) {
        String uploaderId = event.getUploaderId();
        if (uploaderId == null || "anonymous".equals(uploaderId)) return;

        userRepository.findById(uploaderId).ifPresent(user -> {
            user.setPostsCount((user.getPostsCount() != null ? user.getPostsCount() : 0) + 1);
            userRepository.save(user);
            log.debug("[UserStats] Tăng postsCount cho user {}", uploaderId);
        });
    }

    @KafkaListener(
            topics = KafkaTopicConfig.DOCUMENT_INTERACTED_TOPIC,
            groupId = "user-stats-group",
            containerFactory = "kafkaListenerContainerFactory"
    )
    @Transactional
    public void handleDocumentInteracted(DocumentInteractedEvent event) {
        if (!"LIKE".equalsIgnoreCase(event.getType())) return;
        
        String userId = event.getUserId();
        if (userId == null || "anonymous".equals(userId)) return;

        userRepository.findById(userId).ifPresent(user -> {
            int current = user.getLikesCount() != null ? user.getLikesCount() : 0;
            if (event.isAdd()) {
                user.setLikesCount(current + 1);
            } else {
                user.setLikesCount(Math.max(0, current - 1));
            }
            userRepository.save(user);
            log.debug("[UserStats] Cập nhật likesCount cho user {}", userId);
        });
    }

    @KafkaListener(
            topics = KafkaTopicConfig.REVIEW_TOPIC,
            groupId = "user-stats-group",
            containerFactory = "kafkaListenerContainerFactory"
    )
    @Transactional
    public void handleReviewEvent(ReviewEvent event) {
        String userId = event.getUserId();
        if (userId == null || "anonymous".equals(userId)) return;

        userRepository.findById(userId).ifPresent(user -> {
            int current = user.getCommentsCount() != null ? user.getCommentsCount() : 0;
            if (event.isAdd()) {
                user.setCommentsCount(current + 1);
            } else {
                user.setCommentsCount(Math.max(0, current - 1));
            }
            userRepository.save(user);
            log.debug("[UserStats] Cập nhật commentsCount cho user {}", userId);
        });
    }

    @KafkaListener(
            topics = KafkaTopicConfig.USER_FOLLOW_TOPIC,
            groupId = "user-stats-group",
            containerFactory = "kafkaListenerContainerFactory"
    )
    @Transactional
    public void handleUserFollowEvent(UserFollowEvent event) {
        // Cập nhật người đi follow (followingCount)
        userRepository.findById(event.getFollowerId()).ifPresent(follower -> {
            int current = follower.getFollowingCount() != null ? follower.getFollowingCount() : 0;
            follower.setFollowingCount(Math.max(0, event.isAdd() ? current + 1 : current - 1));
            userRepository.save(follower);
        });

        // Cập nhật người được follow (followersCount)
        userRepository.findById(event.getFollowingId()).ifPresent(following -> {
            int current = following.getFollowersCount() != null ? following.getFollowersCount() : 0;
            following.setFollowersCount(Math.max(0, event.isAdd() ? current + 1 : current - 1));
            userRepository.save(following);
        });
        
        log.debug("[UserStats] Cập nhật follow stats cho follower {} và following {}", 
                event.getFollowerId(), event.getFollowingId());
    }
}
