package com.studydocs.modules.system.controller;

import com.studydocs.modules.system.dto.SystemDtos;
import com.studydocs.modules.system.service.NotificationService;
import com.studydocs.shared.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping({"/user/notifications", "/notifications"})
    public ApiResponse<List<SystemDtos.NotificationResponse>> getMyNotifications(Authentication authentication) {
        String userId = authentication != null ? authentication.getName() : "anonymous";
        return ApiResponse.success(notificationService.getMyNotifications(userId));
    }

    @PutMapping("/user/notifications/{id}/read")
    public ApiResponse<String> markAsRead(@PathVariable String id) {
        notificationService.markAsRead(id);
        return ApiResponse.success("Notification marked as read");
    }

    @DeleteMapping("/user/notifications/{id}")
    public ApiResponse<String> deleteNotification(@PathVariable String id) {
        notificationService.deleteNotification(id);
        return ApiResponse.success("Notification deleted successfully");
    }
}
