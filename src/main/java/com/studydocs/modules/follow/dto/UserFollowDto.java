package com.studydocs.modules.follow.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserFollowDto {
    private String id;
    private String name;
    private String avatarUrl;
    private boolean isFollowing;
}
