package com.studydocs.modules.user.service.impl;

import com.studydocs.modules.user.dto.LoginRequest;
import com.studydocs.modules.user.dto.TokenResponseDto;
import com.studydocs.modules.user.dto.UserDto;
import com.studydocs.modules.user.entity.RoleEntity;
import com.studydocs.modules.user.entity.UserEntity;
import com.studydocs.modules.user.repository.RoleRepository;
import com.studydocs.modules.user.repository.UserRepository;
import com.studydocs.modules.user.service.AuthService;
import com.studydocs.modules.user.service.JwtTokenProvider;
import com.studydocs.shared.exception.AppException;
import com.studydocs.shared.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider tokenProvider;

    @Override
    @Transactional
    public TokenResponseDto register(LoginRequest.Register request) {
        String email = request.getEmail();
        if (email == null || email.isBlank()) {
            email = request.getUsername() != null ? request.getUsername() + "@studydocs.com" : "user@studydocs.com";
        }

        if (userRepository.existsByEmail(email)) {
            throw new AppException(ErrorCode.USER_EXISTED);
        }

        RoleEntity userRole = roleRepository.findById("USER")
                .orElseGet(() -> roleRepository.save(RoleEntity.builder().name("USER").description("User role").build()));

        String fullName = request.getFullName() != null ? request.getFullName() : request.getUsername();

        UserEntity user = UserEntity.builder()
                .email(email)
                .password(passwordEncoder.encode(request.getPassword()))
                .fullName(fullName)
                .username(request.getUsername())
                .universityId(request.getUniversityId())
                .universityName(request.getUniversityName())
                .facultyId(request.getFacultyId())
                .major(request.getMajor())
                .roles(Set.of(userRole))
                .isPrivate(false)
                .build();

        user = userRepository.save(user);

        return buildTokenResponse(user);
    }

    @Override
    public TokenResponseDto login(LoginRequest.Login request) {
        String loginIdentifier = request.getEmail() != null ? request.getEmail() : request.getUsername();
        if (loginIdentifier == null || loginIdentifier.isBlank()) {
            throw new AppException(ErrorCode.INVALID_CREDENTIALS);
        }

        UserEntity user = userRepository.findByEmail(loginIdentifier)
                .orElseGet(() -> userRepository.findAll().stream()
                        .filter(u -> loginIdentifier.equalsIgnoreCase(u.getUsername()))
                        .findFirst()
                        .orElseThrow(() -> new AppException(ErrorCode.INVALID_CREDENTIALS)));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new AppException(ErrorCode.INVALID_CREDENTIALS);
        }

        return buildTokenResponse(user);
    }

    @Override
    public TokenResponseDto refreshToken(LoginRequest.RefreshToken request) {
        if (!tokenProvider.validateToken(request.getRefreshToken())) {
            throw new AppException(ErrorCode.INVALID_TOKEN);
        }
        String userId = tokenProvider.getUserIdFromToken(request.getRefreshToken());
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        return buildTokenResponse(user);
    }

    private TokenResponseDto buildTokenResponse(UserEntity user) {
        String token = tokenProvider.generateToken(user.getId(), user.getEmail());
        Set<String> roleNames = user.getRoles() != null ?
                user.getRoles().stream().map(RoleEntity::getName).collect(Collectors.toSet()) : Collections.emptySet();

        String school = user.getUniversityName() != null ? user.getUniversityName() : "Đại học Bách Khoa TP.HCM";

        UserDto userDto = UserDto.builder()
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
                .followersCount(12)
                .followingCount(5)
                .likesCount(30)
                .postsCount(4)
                .commentsCount(8)
                .roles(roleNames)
                .build();

        return TokenResponseDto.builder()
                .accessToken(token)
                .refreshToken(token)
                .tokenType("Bearer")
                .expiresIn(86400)
                .user(userDto)
                .build();
    }
}
