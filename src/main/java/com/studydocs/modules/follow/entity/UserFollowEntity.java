package com.studydocs.modules.follow.entity;

import com.studydocs.shared.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "user_follows")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserFollowEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "follower_id", nullable = false)
    private String followerId;

    @Column(name = "following_id", nullable = false)
    private String followingId;
}
