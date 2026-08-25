package com.studydocs.modules.user.service.impl;

import com.studydocs.modules.user.dto.LoginRequest;
import com.studydocs.modules.user.dto.UserDto;
import com.studydocs.modules.user.entity.RoleEntity;
import com.studydocs.modules.user.entity.UserEntity;
import com.studydocs.modules.user.repository.UserRepository;
import com.studydocs.modules.user.service.UserService;
import com.studydocs.shared.exception.AppException;
import com.studydocs.shared.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import com.studydocs.modules.system.service.MediaService;
import com.studydocs.modules.system.dto.SystemDtos;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final MediaService mediaService;

    @Override
    public UserDto getCurrentUser(String userId) {
        return getUserById(userId);
    }

    @Override
    public UserDto getUserById(String userId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        return toDto(user);
    }

    @Override
    @Transactional
    public UserDto updateProfile(String userId, LoginRequest.UpdateProfile request) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        if (request.getFullName() != null) user.setFullName(request.getFullName());
        if (request.getUsername() != null) user.setUsername(request.getUsername());
        if (request.getPhoneNumber() != null) user.setPhoneNumber(request.getPhoneNumber());
        if (request.getAvatarUrl() != null) user.setAvatarUrl(request.getAvatarUrl());
        if (request.getBio() != null) user.setBio(request.getBio());
        if (request.getGender() != null) user.setGender(request.getGender());
        if (request.getDateOfBirth() != null) user.setDateOfBirth(request.getDateOfBirth());
        if (request.getAddress() != null) user.setAddress(request.getAddress());
        if (request.getSchool() != null) user.setUniversityName(request.getSchool());
        if (request.getUniversityId() != null) user.setUniversityId(request.getUniversityId());
        if (request.getUniversityName() != null) user.setUniversityName(request.getUniversityName());
        if (request.getFacultyId() != null) user.setFacultyId(request.getFacultyId());
        if (request.getMajor() != null) user.setMajor(request.getMajor());
        if (request.getIsPrivate() != null) user.setIsPrivate(request.getIsPrivate());

        user = userRepository.save(user);
        return toDto(user);
    }

    @Override
    @Transactional
    public UserDto updateProfileImage(String userId, MultipartFile file) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        SystemDtos.MediaResponse mediaResponse = mediaService.uploadFile(file, userId);
        user.setAvatarUrl(mediaResponse.getFileUrl());

        user = userRepository.save(user);
        return toDto(user);
    }

    @Override
    public List<UserDto> searchUsers(String query) {
        return userRepository.findAll().stream()
                .filter(u -> (u.getFullName() != null && u.getFullName().toLowerCase().contains(query.toLowerCase()))
                          || (u.getEmail() != null && u.getEmail().toLowerCase().contains(query.toLowerCase())))
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    private UserDto toDto(UserEntity user) {
        Set<String> roleNames = user.getRoles() != null ?
                user.getRoles().stream().map(RoleEntity::getName).collect(Collectors.toSet()) : Collections.emptySet();

        String school = user.getUniversityName() != null ? user.getUniversityName() : "Chưa có trường";

        return UserDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .fullName(user.getFullName())
                .username(user.getUsername())
                .phoneNumber(user.getPhoneNumber())
                .avatarUrl(user.getAvatarUrl())
                .bio(user.getBio())
                .gender(user.getGender())
                .dateOfBirth(user.getDateOfBirth())
                .address(user.getAddress())
                .school(school)
                .universityId(user.getUniversityId())
                .universityName(user.getUniversityName())
                .facultyId(user.getFacultyId())
                .major(user.getMajor())
                .isPrivate(user.getIsPrivate())
                .followersCount(user.getFollowersCount() != null ? user.getFollowersCount() : 0)
                .followingCount(user.getFollowingCount() != null ? user.getFollowingCount() : 0)
                .likesCount(user.getLikesCount() != null ? user.getLikesCount() : 0)
                .postsCount(user.getPostsCount() != null ? user.getPostsCount() : 0)
                .commentsCount(user.getCommentsCount() != null ? user.getCommentsCount() : 0)
                .roles(roleNames)
                .build();
    }
}
