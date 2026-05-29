package studydoc.mapper;

import org.springframework.stereotype.Component;
import studydoc.command.auth.AuthForgotPassword;
import studydoc.command.auth.AuthGoogleCallback;
import studydoc.command.auth.AuthGoogleLogin;
import studydoc.command.auth.AuthLogin;
import studydoc.command.auth.AuthLogout;
import studydoc.command.auth.AuthRefreshToken;
import studydoc.command.auth.AuthRegister;
import studydoc.request.auth.AuthForgotPasswordRequest;
import studydoc.request.auth.AuthGoogleCallbackRequest;
import studydoc.request.auth.AuthGoogleLoginRequest;
import studydoc.request.auth.AuthLoginRequest;
import studydoc.request.auth.AuthLogoutRequest;
import studydoc.request.auth.AuthRefreshTokenRequest;
import studydoc.request.auth.AuthRegisterRequest;

@Component
public class AuthRequestMapper {
    public AuthRegister toRegisterCommand(AuthRegisterRequest request) {
        return AuthRegister.commandOf(
                request.getUsername(),
                request.getPassword(),
                request.getFullName()
        );
    }

    public AuthLogin toLoginCommand(AuthLoginRequest request) {
        return AuthLogin.commandOf(request.getUsername(), request.getPassword());
    }

    public AuthRefreshToken toRefreshTokenCommand(AuthRefreshTokenRequest request) {
        return AuthRefreshToken.commandOf(request.getRefreshToken());
    }

    public AuthLogout toLogoutCommand(AuthLogoutRequest request) {
        return AuthLogout.commandOf(request.getRefreshToken());
    }

    public AuthForgotPassword toForgotPasswordCommand(AuthForgotPasswordRequest request) {
        return AuthForgotPassword.commandOf(request.getUsername());
    }

    public AuthGoogleLogin toGoogleLoginCommand(AuthGoogleLoginRequest request) {
        return AuthGoogleLogin.commandOf(
                request.getRedirectUri(),
                request.getCodeChallenge(),
                request.getCodeChallengeMethod()
        );
    }

    public AuthGoogleCallback toGoogleCallbackCommand(AuthGoogleCallbackRequest request) {
        return AuthGoogleCallback.commandOf(
                request.getCode(),
                request.getCodeVerifier(),
                request.getRedirectUri()
        );
    }
}
