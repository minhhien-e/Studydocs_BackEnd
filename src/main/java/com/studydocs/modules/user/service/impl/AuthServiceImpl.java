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
import com.studydocs.modules.user.service.MailService;
import com.studydocs.shared.exception.AppException;
import com.studydocs.shared.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Random;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import org.springframework.beans.factory.annotation.Value;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider tokenProvider;
    private final MailService mailService;

    @Value("${app.google.client-id:}")
    private String googleClientId;

    @Value("${app.google.web-client-id:}")
    private String googleWebClientId;

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

    @Override
    @Transactional
    public TokenResponseDto googleLogin(String idTokenString) {
        try {
            GoogleIdTokenVerifier.Builder verifierBuilder = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new GsonFactory());
            
            java.util.List<String> audiences = new java.util.ArrayList<>();
            if (googleClientId != null && !googleClientId.isEmpty()) {
                for (String id : googleClientId.split(",")) {
                    audiences.add(id.trim());
                }
            }
            if (googleWebClientId != null && !googleWebClientId.isEmpty()) {
                for (String id : googleWebClientId.split(",")) {
                    audiences.add(id.trim());
                }
            }
            // If both are missing, we could fallback to checking the frontend clientId, 
            // but for safety we only set it if available
            if (!audiences.isEmpty()) {
                verifierBuilder.setAudience(audiences);
            }
            
            GoogleIdTokenVerifier verifier = verifierBuilder.build();

            GoogleIdToken idToken = verifier.verify(idTokenString);
            if (idToken != null) {
                GoogleIdToken.Payload payload = idToken.getPayload();
                String email = payload.getEmail();
                String name = (String) payload.get("name");
                String pictureUrl = (String) payload.get("picture");

                if (email == null) {
                    throw new AppException(ErrorCode.INVALID_TOKEN);
                }

                Optional<UserEntity> userOpt = userRepository.findByEmail(email);
                UserEntity user;
                if (userOpt.isPresent()) {
                    user = userOpt.get();
                } else {
                    RoleEntity userRole = roleRepository.findById("USER")
                            .orElseGet(() -> roleRepository.save(RoleEntity.builder().name("USER").description("User role").build()));
                            
                    String generatedUsername = email.split("@")[0] + "_" + UUID.randomUUID().toString().substring(0, 5);

                    user = UserEntity.builder()
                            .email(email)
                            .password(passwordEncoder.encode(UUID.randomUUID().toString()))
                            .fullName(name != null ? name : generatedUsername)
                            .username(generatedUsername)
                            .roles(Set.of(userRole))
                            .avatarUrl(pictureUrl)
                            .isPrivate(false)
                            .build();
                    user = userRepository.save(user);
                }
                
                return buildTokenResponse(user);
            } else {
                throw new AppException(ErrorCode.INVALID_TOKEN);
            }
        } catch (Exception e) {
            throw new AppException(ErrorCode.INVALID_TOKEN);
        }
    }

    @Override
    @Transactional
    public void forgotPassword(String email) {
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        String otp = String.format("%06d", new Random().nextInt(999999));
        user.setResetToken(otp);
        user.setResetTokenExpiry(LocalDateTime.now().plusMinutes(15));
        userRepository.save(user);

        mailService.sendPasswordResetToken(email, otp);
    }

    @Override
    public void verifyResetToken(String email, String token) {
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        if (user.getResetToken() == null || !user.getResetToken().equals(token)) {
            throw new AppException(ErrorCode.INVALID_TOKEN); // Or custom INVALID_OTP
        }

        if (user.getResetTokenExpiry() == null || user.getResetTokenExpiry().isBefore(LocalDateTime.now())) {
            throw new AppException(ErrorCode.INVALID_TOKEN); // Or custom OTP_EXPIRED
        }
    }

    @Override
    @Transactional
    public void resetPassword(String email, String token, String newPassword) {
        verifyResetToken(email, token); // ensure valid before updating

        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        user.setPassword(passwordEncoder.encode(newPassword));
        user.setResetToken(null);
        user.setResetTokenExpiry(null);
        userRepository.save(user);
    }

    private TokenResponseDto buildTokenResponse(UserEntity user) {
        String token = tokenProvider.generateToken(user.getId(), user.getEmail());
        Set<String> roleNames = user.getRoles() != null ?
                user.getRoles().stream().map(RoleEntity::getName).collect(Collectors.toSet()) : Collections.emptySet();

        String school = user.getUniversityName();

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
                .followersCount(user.getFollowersCount())
                .followingCount(user.getFollowingCount())
                .likesCount(user.getLikesCount())
                .postsCount(user.getPostsCount())
                .commentsCount(user.getCommentsCount())
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
