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
        followService.followUser(followerId, targetUserId);
        return ApiResponse.success("Followed user successfully");
    }

    @PostMapping({"/{targetUserId}/unfollow", "/follows/{targetUserId}/unfollow"})
    public ApiResponse<String> unfollowUser(Authentication authentication, @PathVariable String targetUserId) {
        String followerId = authentication != null ? authentication.getName() : "anonymous";
        followService.unfollowUser(followerId, targetUserId);
        return ApiResponse.success("Unfollowed user successfully");
    }

    @DeleteMapping({"/{targetUserId}/follow", "/follows/{targetUserId}"})
    public ApiResponse<String> unfollowUserDelete(Authentication authentication, @PathVariable String targetUserId) {
        String followerId = authentication != null ? authentication.getName() : "anonymous";
        followService.unfollowUser(followerId, targetUserId);
        return ApiResponse.success("Unfollowed user successfully");
    }

    @GetMapping({"/followers", "/follows/followers", "/{userId}/followers", "/public/{userId}/followers"})
    public ApiResponse<List<com.studydocs.modules.follow.dto.UserFollowDto>> getFollowers(Authentication authentication, @PathVariable(required = false) String userId) {
        String currentUserId = authentication != null ? authentication.getName() : "anonymous";
        String targetUserId = userId != null ? userId : currentUserId;
        return ApiResponse.success(followService.getFollowers(targetUserId, currentUserId));
    }

    @GetMapping({"/following", "/follows/following", "/{userId}/following", "/public/{userId}/following"})
    public ApiResponse<List<com.studydocs.modules.follow.dto.UserFollowDto>> getFollowing(Authentication authentication, @PathVariable(required = false) String userId) {
        String currentUserId = authentication != null ? authentication.getName() : "anonymous";
        String targetUserId = userId != null ? userId : currentUserId;
        return ApiResponse.success(followService.getFollowing(targetUserId, currentUserId));
    }
}
