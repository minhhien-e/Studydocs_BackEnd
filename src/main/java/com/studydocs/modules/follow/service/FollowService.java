package com.studydocs.modules.follow.service;

import java.util.List;

public interface FollowService {
    void followUser(String followerId, String targetUserId);
    void unfollowUser(String followerId, String targetUserId);
    List<com.studydocs.modules.follow.dto.UserFollowDto> getFollowers(String userId, String currentUserId);
    List<com.studydocs.modules.follow.dto.UserFollowDto> getFollowing(String userId, String currentUserId);
}
