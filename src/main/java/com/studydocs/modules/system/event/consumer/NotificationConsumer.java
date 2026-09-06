package com.studydocs.modules.system.event.consumer;

import com.studydocs.config.KafkaTopicConfig;
import com.studydocs.modules.follow.repository.FollowRepository;
import com.studydocs.modules.system.entity.NotificationEntity;
import com.studydocs.modules.system.repository.NotificationRepository;
import com.studydocs.modules.user.entity.UserEntity;
import com.studydocs.modules.user.repository.UserRepository;
import com.studydocs.shared.event.NotificationEvent;
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
                        .build();

                notificationRepository.save(notification);
            });

            log.info("[Notification Consumer] Đã xử lý xong NotificationEvent cho documentId={}", event.getDocumentId());
        } catch (Exception e) {
            log.error("[Notification Consumer] Lỗi khi xử lý NotificationEvent: {}", e.getMessage(), e);
        }
    }
}
