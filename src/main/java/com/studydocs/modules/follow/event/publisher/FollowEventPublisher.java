package com.studydocs.modules.follow.event.publisher;

public interface FollowEventPublisher {
    
    /**
     * Publish event khi người dùng thực hiện follow hoặc unfollow.
     *
     * @param followerId  ID của người đi follow
     * @param followingId ID của người được follow
     * @param isAdd       true nếu là follow, false nếu là unfollow
     */
    void publishFollowEvent(String followerId, String followingId, boolean isAdd);
}
