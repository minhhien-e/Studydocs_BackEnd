package com.studydocs.modules.follow.repository;

import com.studydocs.modules.follow.entity.UserFollowEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FollowRepository extends JpaRepository<UserFollowEntity, Long> {
    List<UserFollowEntity> findByFollowerId(String followerId);
    List<UserFollowEntity> findByFollowingId(String followingId);
    Optional<UserFollowEntity> findByFollowerIdAndFollowingId(String followerId, String followingId);
    boolean existsByFollowerIdAndFollowingId(String followerId, String followingId);
}
