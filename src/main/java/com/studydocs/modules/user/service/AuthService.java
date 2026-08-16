package com.studydocs.modules.user.service;

import com.studydocs.modules.user.dto.LoginRequest;
import com.studydocs.modules.user.dto.TokenResponseDto;

public interface AuthService {
    TokenResponseDto login(LoginRequest.Login request);
    TokenResponseDto register(LoginRequest.Register request);
    TokenResponseDto refreshToken(LoginRequest.RefreshToken request);
}
