package com.studydocs.shared.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserFollowEvent {
    private String followerId;
    private String followingId;
    
    /** 
     * true: follow
     * false: unfollow
     */
    private boolean isAdd;
}
