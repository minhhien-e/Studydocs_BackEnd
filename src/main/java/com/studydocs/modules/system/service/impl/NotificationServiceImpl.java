package com.studydocs.modules.system.service.impl;

import com.studydocs.modules.system.dto.SystemDtos;
import com.studydocs.modules.system.entity.NotificationEntity;
import com.studydocs.modules.system.repository.NotificationRepository;
import com.studydocs.modules.system.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;

    @Override
    public List<SystemDtos.NotificationResponse> getMyNotifications(String userId) {
        return notificationRepository.findByRecipientIdAndIsTrashedFalseOrderByCreatedAtDesc(userId).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<SystemDtos.NotificationResponse> getTrashedNotifications(String userId) {
        return notificationRepository.findByRecipientIdAndIsTrashedTrueOrderByCreatedAtDesc(userId).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public void markAsRead(String notificationId) {
        NotificationEntity n = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new com.studydocs.shared.exception.AppException(com.studydocs.shared.exception.ErrorCode.NOTIFICATION_NOT_FOUND));
        n.setIsRead(true);
        notificationRepository.save(n);
    }

    @Override
    public void deleteNotification(String notificationId) {
        notificationRepository.findById(notificationId).ifPresent(n -> {
            n.setIsTrashed(true);
            notificationRepository.save(n);
        });
    }

    @Override
    public void restoreNotification(String notificationId) {
        notificationRepository.findById(notificationId).ifPresent(n -> {
            n.setIsTrashed(false);
            notificationRepository.save(n);
        });
    }

    @Override
    public void hardDeleteNotification(String notificationId) {
        if (notificationRepository.existsById(notificationId)) {
            notificationRepository.deleteById(notificationId);
        }
    }

    @Override
    public void emptyTrash(String userId) {
        List<NotificationEntity> trashed = notificationRepository.findByRecipientIdAndIsTrashedTrueOrderByCreatedAtDesc(userId);
        notificationRepository.deleteAll(trashed);
    }

    private SystemDtos.NotificationResponse toDto(NotificationEntity n) {
        return SystemDtos.NotificationResponse.builder()
                .id(n.getId())
                .recipientId(n.getRecipientId())
                .title(n.getTitle())
                .content(n.getContent())
                .isRead(n.getIsRead())
                .type(n.getType() != null ? n.getType() : "system")
                .senderId(n.getSenderId())
                .referenceId(n.getReferenceId())
                .isTrashed(n.getIsTrashed())
                .createdAt(n.getCreatedAt())
                .build();
    }
}
