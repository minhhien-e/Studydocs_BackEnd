package com.studydocs.modules.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private String id;
    private String email;
    private String fullName;
    private String username;
    private String phoneNumber;
    private String avatarUrl;
    private String bio;
    private String gender;
    private LocalDateTime dateOfBirth;
    private String address;
    private String school;
    private Long universityId;
    private String universityName;
    private Long facultyId;
    private String major;
    private Boolean isPrivate;
    
    @Builder.Default
    private Integer followersCount = 0;
    
    @Builder.Default
    private Integer followingCount = 0;

    @Builder.Default
    private Integer likesCount = 0;

    @Builder.Default
    private Integer postsCount = 0;

    @Builder.Default
    private Integer commentsCount = 0;

    private Set<String> roles;
}
