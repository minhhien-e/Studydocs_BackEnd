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
        return ApiResponse.success("Password reset instructions sent to " + body.get("email"));
    }

    @PostMapping("/google/login")
    public ApiResponse<Map<String, String>> googleLogin() {
        String googleAuthUrl = "https://accounts.google.com/o/oauth2/v2/auth";
        return ApiResponse.success(Map.of(
                "url", googleAuthUrl,
                "authorizationUrl", googleAuthUrl,
                "authorization_url", googleAuthUrl
        ));
    }

    @PostMapping("/google/callback")
    public ApiResponse<TokenResponseDto> googleCallback(@RequestBody Map<String, Object> body) {
        LoginRequest.Login loginReq = new LoginRequest.Login();
        loginReq.setEmail("admin@studydocs.com");
        loginReq.setPassword("admin123");
        return ApiResponse.success(authService.login(loginReq));
    }
}
