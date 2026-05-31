package studydoc.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import studydoc.bus.SimpleUserCommandBus;
import studydoc.mapper.AuthRequestMapper;
import studydoc.request.auth.AuthForgotPasswordRequest;
import studydoc.request.auth.AuthGoogleCallbackRequest;
import studydoc.request.auth.AuthGoogleLoginRequest;
import studydoc.request.auth.AuthLoginRequest;
import studydoc.request.auth.AuthLogoutRequest;
import studydoc.request.auth.AuthRefreshTokenRequest;
import studydoc.request.auth.AuthRegisterRequest;
import studydoc.response.ApiResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/public/auth")
public class PublicAuthController {
    private final SimpleUserCommandBus commandBus;
    private final AuthRequestMapper authRequestMapper;

    @PostMapping("/register")
    public ApiResponse<?> register(@Valid @RequestBody AuthRegisterRequest request) {
        commandBus.send(authRequestMapper.toRegisterCommand(request));
        return ApiResponse.success(null);
    }

    @PostMapping("/login")
    public ApiResponse<?> login(@Valid @RequestBody AuthLoginRequest request) {
        var result = commandBus.send(authRequestMapper.toLoginCommand(request));
        return ApiResponse.success(result);
    }

    @PostMapping("/refresh-token")
    public ApiResponse<?> refreshToken(@Valid @RequestBody AuthRefreshTokenRequest request) {
        var result = commandBus.send(authRequestMapper.toRefreshTokenCommand(request));
        return ApiResponse.success(result);
    }

    @PostMapping("/logout")
    public ApiResponse<?> logout(@Valid @RequestBody AuthLogoutRequest request) {
        commandBus.send(authRequestMapper.toLogoutCommand(request));
        return ApiResponse.success(null);
    }

    @PostMapping("/forgot-password")
    public ApiResponse<?> forgotPassword(@Valid @RequestBody AuthForgotPasswordRequest request) {
        commandBus.send(authRequestMapper.toForgotPasswordCommand(request));
        return ApiResponse.success(null);
    }

    @PostMapping("/google/login")
    public ApiResponse<?> googleLogin(@Valid @RequestBody AuthGoogleLoginRequest request) {
        var result = commandBus.send(authRequestMapper.toGoogleLoginCommand(request));
        return ApiResponse.success(result);
    }

    @PostMapping("/google/callback")
    public ApiResponse<?> googleCallback(@Valid @RequestBody AuthGoogleCallbackRequest request) {
        var result = commandBus.send(authRequestMapper.toGoogleCallbackCommand(request));
        return ApiResponse.success(result);
    }
}
