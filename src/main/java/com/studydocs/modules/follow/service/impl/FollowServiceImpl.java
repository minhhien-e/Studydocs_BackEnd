package com.studydocs.modules.follow.service.impl;

import com.studydocs.modules.follow.entity.UserFollowEntity;
import com.studydocs.modules.follow.repository.FollowRepository;
import com.studydocs.modules.follow.service.FollowService;
import com.studydocs.modules.follow.event.publisher.FollowEventPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import java.util.stream.Collectors;
import com.studydocs.modules.follow.dto.UserFollowDto;
import com.studydocs.modules.user.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class FollowServiceImpl implements FollowService {

    private final FollowRepository followRepository;
    private final FollowEventPublisher followEventPublisher;
    private final UserRepository userRepository;

    @Override
    public void followUser(String followerId, String targetUserId) {
        if (followerId != null && followerId.equals(targetUserId)) {
            throw new com.studydocs.shared.exception.AppException(
                    com.studydocs.shared.exception.ErrorCode.CANNOT_FOLLOW_SELF);
        }
        if (followRepository.existsByFollowerIdAndFollowingId(followerId, targetUserId)) {
            throw new com.studydocs.shared.exception.AppException(
                    com.studydocs.shared.exception.ErrorCode.ALREADY_FOLLOWED);
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
    public List<UserFollowDto> getFollowers(String userId, String currentUserId) {
        return followRepository.findByFollowingId(userId).stream()
                .map(follow -> buildUserFollowDto(follow.getFollowerId(), currentUserId))
                .filter(dto -> dto != null)
                .collect(Collectors.toList());
    }

    @Override
    public List<UserFollowDto> getFollowing(String userId, String currentUserId) {
        return followRepository.findByFollowerId(userId).stream()
                .map(follow -> buildUserFollowDto(follow.getFollowingId(), currentUserId))
                .filter(dto -> dto != null)
                .collect(Collectors.toList());
    }

    private UserFollowDto buildUserFollowDto(String targetUserId, String currentUserId) {
        return userRepository.findById(targetUserId).map(user -> {
            boolean isFollowing = false;
            if (currentUserId != null && !currentUserId.equals("anonymous") && !currentUserId.equals("anonymousUser")) {
                isFollowing = followRepository.existsByFollowerIdAndFollowingId(currentUserId, targetUserId);
            }
            return UserFollowDto.builder()
                    .id(user.getId())
                    .name(user.getFullName() != null && !user.getFullName().isEmpty() ? user.getFullName()
                            : user.getUsername())
                    .avatarUrl(user.getAvatarUrl())
                    .isFollowing(isFollowing)
                    .build();
        }).orElse(null);
    }
}
