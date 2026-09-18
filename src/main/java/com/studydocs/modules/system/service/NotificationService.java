package com.studydocs.modules.system.service;

import com.studydocs.modules.system.dto.SystemDtos;

import java.util.List;

public interface NotificationService {
    List<SystemDtos.NotificationResponse> getMyNotifications(String userId);
    List<SystemDtos.NotificationResponse> getTrashedNotifications(String userId);
    void markAsRead(String notificationId);
    void deleteNotification(String notificationId);
    void restoreNotification(String notificationId);
    void hardDeleteNotification(String notificationId);
    void emptyTrash(String userId);
}
