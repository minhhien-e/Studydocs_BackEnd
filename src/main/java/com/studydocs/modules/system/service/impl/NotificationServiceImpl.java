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
        return notificationRepository.findByRecipientIdOrderByCreatedAtDesc(userId).stream()
                .map(n -> SystemDtos.NotificationResponse.builder()
                        .id(n.getId())
                        .recipientId(n.getRecipientId())
                        .title(n.getTitle())
                        .content(n.getContent())
                        .isRead(n.getIsRead())
                        .createdAt(n.getCreatedAt())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public void markAsRead(String notificationId) {
        com.studydocs.modules.system.entity.NotificationEntity n = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new com.studydocs.shared.exception.AppException(com.studydocs.shared.exception.ErrorCode.NOTIFICATION_NOT_FOUND));
        n.setIsRead(true);
        notificationRepository.save(n);
    }
}
