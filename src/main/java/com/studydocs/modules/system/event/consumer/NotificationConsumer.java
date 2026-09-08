package com.studydocs.modules.system.event.consumer;

import com.studydocs.config.KafkaTopicConfig;
import com.studydocs.modules.follow.repository.FollowRepository;
import com.studydocs.modules.system.entity.NotificationEntity;
import com.studydocs.modules.system.repository.NotificationRepository;
import com.studydocs.modules.user.entity.UserEntity;
import com.studydocs.modules.user.repository.UserRepository;
import com.studydocs.shared.event.NotificationEvent;
import com.studydocs.shared.event.DocumentInteractedEvent;
import com.studydocs.shared.event.UserFollowEvent;
import com.studydocs.shared.event.ReviewEvent;
import com.studydocs.modules.academic.repository.DocumentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationConsumer {

    private final NotificationRepository notificationRepository;
    private final FollowRepository followRepository;
    private final UserRepository userRepository;
    private final DocumentRepository documentRepository;

    @KafkaListener(
            topics = KafkaTopicConfig.NOTIFICATION_TOPIC,
            groupId = "notification-group",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void consumeNotificationEvent(NotificationEvent event) {
        log.info("[Notification Consumer] Nhận được sự kiện NotificationEvent: {}", event);

        try {
            String uploaderId = event.getUploaderId();
            if (uploaderId == null || uploaderId.equals("anonymous")) {
                log.info("[Notification Consumer] Bỏ qua vì uploader vô danh.");
                return;
            }

            // Lấy thông tin người đăng (tuỳ chọn)
            String uploaderName = userRepository.findById(uploaderId)
                    .map(UserEntity::getFullName)
                    .orElse("Một người dùng");

            // Tìm tất cả những người đang theo dõi người đăng bài
            followRepository.findByFollowingId(uploaderId).forEach(follow -> {
                String followerId = follow.getFollowerId();

                // Bỏ qua nếu followerId không hợp lệ
                if (followerId == null || followerId.isEmpty()) {
                    return;
                }

                String documentTitle = event.getDocumentTitle();
                if (documentTitle == null || documentTitle.isEmpty()) {
                    documentTitle = "tài liệu mới";
                }

                // Tạo thông báo
                NotificationEntity notification = NotificationEntity.builder()
                        .recipientId(followerId)
                        .title("Tài liệu mới từ người bạn theo dõi")
                        .content(uploaderName + " vừa đăng: " + documentTitle)
                        .isRead(false)
                        .type("SYSTEM")
                        .senderId(uploaderId)
                        .referenceId(event.getDocumentId())
                        .build();

                notificationRepository.save(notification);
            });

            log.info("[Notification Consumer] Đã xử lý xong NotificationEvent cho documentId={}", event.getDocumentId());
        } catch (Exception e) {
            log.error("[Notification Consumer] Lỗi khi xử lý NotificationEvent: {}", e.getMessage(), e);
        }
    }

    @KafkaListener(
            topics = KafkaTopicConfig.DOCUMENT_INTERACTED_TOPIC,
            groupId = "notification-group",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void consumeDocumentInteractedEvent(DocumentInteractedEvent event) {
        log.info("[Notification Consumer] Nhận được sự kiện DocumentInteractedEvent: {}", event);

        try {
            if ("LIKE".equalsIgnoreCase(event.getType())) {
                documentRepository.findById(event.getDocumentId()).ifPresent(doc -> {
                    String ownerId = doc.getUploaderId();
                    if (ownerId == null || ownerId.equals(event.getUserId())) return;

                    if (event.isAdd()) {
                        String senderName = userRepository.findById(event.getUserId())
                                .map(UserEntity::getFullName)
                                .orElse("Một người dùng");

                        NotificationEntity notification = NotificationEntity.builder()
                                .recipientId(ownerId)
                                .title("Có người thích tài liệu của bạn")
                                .content(senderName + " đã thích tài liệu: " + doc.getTitle())
                                .isRead(false)
                                .type("LIKE")
                                .senderId(event.getUserId())
                                .referenceId(event.getDocumentId())
                                .build();
                        notificationRepository.save(notification);
                    } else {
                        notificationRepository.deleteByRecipientIdAndTypeAndReferenceId(ownerId, "LIKE", event.getDocumentId());
                    }
                });
            }
        } catch (Exception e) {
            log.error("[Notification Consumer] Lỗi khi xử lý DocumentInteractedEvent: {}", e.getMessage(), e);
        }
    }

    @KafkaListener(
            topics = KafkaTopicConfig.REVIEW_TOPIC,
            groupId = "notification-group",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void consumeReviewEvent(ReviewEvent event) {
        log.info("[Notification Consumer] Nhận được sự kiện ReviewEvent: {}", event);

        try {
            documentRepository.findById(event.getDocumentId()).ifPresent(doc -> {
                String ownerId = doc.getUploaderId();
                if (ownerId == null || ownerId.equals(event.getUserId())) return;

                if (event.isAdd()) {
                    String senderName = userRepository.findById(event.getUserId())
                            .map(UserEntity::getFullName)
                            .orElse("Một người dùng");

                    NotificationEntity notification = NotificationEntity.builder()
                            .recipientId(ownerId)
                            .title("Có người bình luận tài liệu của bạn")
                            .content(senderName + " đã bình luận về tài liệu: " + doc.getTitle())
                            .isRead(false)
                            .type("COMMENT")
                            .senderId(event.getUserId())
                            .referenceId(event.getDocumentId())
                            .build();
                    notificationRepository.save(notification);
                } else {
                    notificationRepository.deleteByRecipientIdAndTypeAndReferenceId(ownerId, "COMMENT", event.getDocumentId());
                }
            });
        } catch (Exception e) {
            log.error("[Notification Consumer] Lỗi khi xử lý ReviewEvent: {}", e.getMessage(), e);
        }
    }

    @KafkaListener(
            topics = KafkaTopicConfig.USER_FOLLOW_TOPIC,
            groupId = "notification-group",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void consumeUserFollowEvent(UserFollowEvent event) {
        log.info("[Notification Consumer] Nhận được sự kiện UserFollowEvent: {}", event);

        try {
            if (event.isAdd()) {
                String senderName = userRepository.findById(event.getFollowerId())
                        .map(UserEntity::getFullName)
                        .orElse("Một người dùng");

                NotificationEntity notification = NotificationEntity.builder()
                        .recipientId(event.getFollowingId())
                        .title("Người theo dõi mới")
                        .content(senderName + " đã bắt đầu theo dõi bạn.")
                        .isRead(false)
                        .type("FOLLOW")
                        .senderId(event.getFollowerId())
                        .referenceId(event.getFollowerId())
                        .build();
                notificationRepository.save(notification);
            } else {
                notificationRepository.deleteByRecipientIdAndTypeAndReferenceId(event.getFollowingId(), "FOLLOW", event.getFollowerId());
            }
        } catch (Exception e) {
            log.error("[Notification Consumer] Lỗi khi xử lý UserFollowEvent: {}", e.getMessage(), e);
        }
    }
}
