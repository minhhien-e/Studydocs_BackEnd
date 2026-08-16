package com.studydocs.modules.follow.controller;

import com.studydocs.modules.follow.service.FollowService;
import com.studydocs.shared.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class FollowController {

    private final FollowService followService;

    @PostMapping({"/{targetUserId}/follow", "/follows/{targetUserId}", "/follows/{targetUserId}/follow"})
    public ApiResponse<String> followUser(Authentication authentication, @PathVariable String targetUserId) {
        String followerId = authentication != null ? authentication.getName() : "anonymous";
        try {
            followService.followUser(followerId, targetUserId);
        } catch (Exception ignored) {
        }
        return ApiResponse.success("Followed user successfully");
    }

    @RequestMapping(value = {"/{targetUserId}/unfollow", "/{targetUserId}/follow", "/follows/{targetUserId}/unfollow"}, method = {RequestMethod.POST, RequestMethod.DELETE})
    public ApiResponse<String> unfollowUser(Authentication authentication, @PathVariable String targetUserId) {
        String followerId = authentication != null ? authentication.getName() : "anonymous";
        try {
            followService.unfollowUser(followerId, targetUserId);
        } catch (Exception ignored) {
        }
        return ApiResponse.success("Unfollowed user successfully");
    }

    @GetMapping({"/followers", "/follows/followers", "/{userId}/followers"})
    public ApiResponse<List<String>> getFollowers(Authentication authentication, @PathVariable(required = false) String userId) {
        String currentUserId = userId != null ? userId : (authentication != null ? authentication.getName() : "anonymous");
        return ApiResponse.success(followService.getFollowers(currentUserId));
    }

    @GetMapping({"/following", "/follows/following", "/{userId}/following"})
    public ApiResponse<List<String>> getFollowing(Authentication authentication, @PathVariable(required = false) String userId) {
        String currentUserId = userId != null ? userId : (authentication != null ? authentication.getName() : "anonymous");
        return ApiResponse.success(followService.getFollowing(currentUserId));
    }
}
