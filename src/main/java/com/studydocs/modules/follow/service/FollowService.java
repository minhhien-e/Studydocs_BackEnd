package com.studydocs.modules.follow.service;

import java.util.List;

public interface FollowService {
    void followUser(String followerId, String targetUserId);
    void unfollowUser(String followerId, String targetUserId);
    List<String> getFollowers(String userId);
    List<String> getFollowing(String userId);
}
