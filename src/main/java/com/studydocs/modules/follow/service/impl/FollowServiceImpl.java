package com.studydocs.modules.follow.service.impl;

import com.studydocs.modules.follow.entity.UserFollowEntity;
import com.studydocs.modules.follow.repository.FollowRepository;
import com.studydocs.modules.follow.service.FollowService;
import com.studydocs.modules.follow.event.publisher.FollowEventPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FollowServiceImpl implements FollowService {

    private final FollowRepository followRepository;
    private final FollowEventPublisher followEventPublisher;

    @Override
    public void followUser(String followerId, String targetUserId) {
        if (followerId != null && followerId.equals(targetUserId)) {
            throw new com.studydocs.shared.exception.AppException(com.studydocs.shared.exception.ErrorCode.CANNOT_FOLLOW_SELF);
        }
        if (followRepository.existsByFollowerIdAndFollowingId(followerId, targetUserId)) {
            throw new com.studydocs.shared.exception.AppException(com.studydocs.shared.exception.ErrorCode.ALREADY_FOLLOWED);
        }
        UserFollowEntity follow = UserFollowEntity.builder()
                .followerId(followerId)
                .followingId(targetUserId)
                .build();
        followRepository.save(follow);
        
        followEventPublisher.publishFollowEvent(followerId, targetUserId, true);
    }

    @Override
    public void unfollowUser(String followerId, String targetUserId) {
        followRepository.findByFollowerIdAndFollowingId(followerId, targetUserId)
                .ifPresent(follow -> {
                    followRepository.delete(follow);
                    followEventPublisher.publishFollowEvent(followerId, targetUserId, false);
                });
    }

    @Override
    public List<String> getFollowers(String userId) {
        return followRepository.findByFollowingId(userId).stream()
                .map(UserFollowEntity::getFollowerId)
                .collect(Collectors.toList());
    }

    @Override
    public List<String> getFollowing(String userId) {
        return followRepository.findByFollowerId(userId).stream()
                .map(UserFollowEntity::getFollowerId)
                .collect(Collectors.toList());
    }
}
