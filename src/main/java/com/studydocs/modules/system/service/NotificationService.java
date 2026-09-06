package com.studydocs.modules.system.service;

import com.studydocs.modules.system.dto.SystemDtos;

import java.util.List;

public interface NotificationService {
    List<SystemDtos.NotificationResponse> getMyNotifications(String userId);
    void markAsRead(String notificationId);
    void deleteNotification(String notificationId);
}
