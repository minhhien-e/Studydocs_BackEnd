package studydoc.handler.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import studydoc.command.auth.AuthGoogleCallback;
import studydoc.dto.auth.TokenResponseDTO;
import studydoc.handler.CommandHandler;
import studydoc.mapper.AuthMapper;
import studydoc.port.KeycloakAuthPort;
import studydoc.port.KeycloakUserInfo;
import studydoc.port.TokenResponse;
import studydoc.service.UserProfileSyncService;

@Component
@RequiredArgsConstructor
public class GoogleCallbackAuthHandler implements CommandHandler<AuthGoogleCallback, TokenResponseDTO> {
    private final KeycloakAuthPort keycloakAuthPort;
    private final AuthMapper authMapper;
    private final UserProfileSyncService userProfileSyncService;

    @Override
    public TokenResponseDTO handle(AuthGoogleCallback command) {
        TokenResponse tokenResponse = keycloakAuthPort.exchangeAuthorizationCode(
                command.getCode(),
                command.getRedirectUri(),
                command.getCodeVerifier()
        );

        KeycloakUserInfo userInfo = keycloakAuthPort.getUserInfo(tokenResponse.accessToken());
        userProfileSyncService.ensureProfileExists(userInfo);

        return authMapper.toTokenResponseDTO(tokenResponse);
    }

    @Override
    public Class<AuthGoogleCallback> commandType() {
        return AuthGoogleCallback.class;
    }
}
