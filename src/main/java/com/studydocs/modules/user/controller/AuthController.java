package com.studydocs.modules.user.controller;

import com.studydocs.modules.user.dto.LoginRequest;
import com.studydocs.modules.user.dto.TokenResponseDto;
import com.studydocs.modules.user.service.AuthService;
import com.studydocs.shared.dto.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Controller xử lý các API xác thực tài khoản.
 *
 * @author StudyDocs Team
 * @since 1.0.0
 */
@RestController
@RequestMapping("/user/public/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ApiResponse<TokenResponseDto> login(@Valid @RequestBody LoginRequest.Login request) {
        return ApiResponse.success(authService.login(request));
    }

    @PostMapping("/register")
    public ApiResponse<TokenResponseDto> register(@Valid @RequestBody LoginRequest.Register request) {
        return ApiResponse.success(authService.register(request));
    }

    @PostMapping("/refresh-token")
    public ApiResponse<TokenResponseDto> refreshToken(@Valid @RequestBody LoginRequest.RefreshToken request) {
        return ApiResponse.success(authService.refreshToken(request));
    }

    @PostMapping("/logout")
    public ApiResponse<String> logout() {
        return ApiResponse.success("Logged out successfully");
    }

    @PostMapping("/forgot-password")
    public ApiResponse<String> forgotPassword(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        if (email == null || email.isBlank()) {
            throw new com.studydocs.shared.exception.AppException(com.studydocs.shared.exception.ErrorCode.INVALID_REQUEST);
        }
        authService.forgotPassword(email);
        return ApiResponse.success("OTP sent to " + email);
    }

    @PostMapping("/verify-reset-token")
    public ApiResponse<String> verifyResetToken(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        String token = body.get("token");
        if (email == null || token == null || email.isBlank() || token.isBlank()) {
            throw new com.studydocs.shared.exception.AppException(com.studydocs.shared.exception.ErrorCode.INVALID_REQUEST);
        }
        authService.verifyResetToken(email, token);
        return ApiResponse.success("Token verified");
    }

    @PostMapping("/reset-password")
    public ApiResponse<String> resetPassword(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        String token = body.get("token");
        String newPassword = body.get("newPassword");
        if (email == null || token == null || newPassword == null || email.isBlank() || token.isBlank() || newPassword.isBlank()) {
            throw new com.studydocs.shared.exception.AppException(com.studydocs.shared.exception.ErrorCode.INVALID_REQUEST);
        }
        authService.resetPassword(email, token, newPassword);
        return ApiResponse.success("Password reset successfully");
    }

    @PostMapping("/google/callback")
    public ApiResponse<TokenResponseDto> googleCallback(@RequestBody Map<String, Object> body) {
        String idToken = (String) body.get("idToken");
        if (idToken == null || idToken.isBlank()) {
            throw new com.studydocs.shared.exception.AppException(com.studydocs.shared.exception.ErrorCode.INVALID_REQUEST);
        }
        return ApiResponse.success(authService.googleLogin(idToken));
    }
}
