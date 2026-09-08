package com.studydocs.modules.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDateTime;

public class LoginRequest {

    @Data
    public static class Login {
        private String username;

        @Email(message = "INVALID_REQUEST")
        private String email;

        @NotBlank(message = "INVALID_REQUEST")
        private String password;
    }

    @Data
    public static class Register {
        private String username;

        @Email(message = "INVALID_REQUEST")
        private String email;

        @NotBlank(message = "INVALID_REQUEST")
        private String password;

        private String fullName;
        private Long universityId;
        private String universityName;
        private Long facultyId;
        private String major;
    }

    @Data
    public static class RefreshToken {
        @NotBlank(message = "INVALID_REQUEST")
        private String refreshToken;
    }

    @Data
    public static class UpdateProfile {
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
    }

    @Data
    public static class UpdateEmailRequest {
        @Email(message = "INVALID_REQUEST")
        @NotBlank(message = "INVALID_REQUEST")
        private String email;
    }

    @Data
    public static class UpdateEmailVerify {
        @NotBlank(message = "INVALID_REQUEST")
        private String token;
    }
}
