package studydoc.handler.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import studydoc.command.auth.AuthGoogleLogin;
import studydoc.dto.auth.GoogleAuthUrlDTO;
import studydoc.handler.CommandHandler;
import studydoc.mapper.AuthMapper;
import studydoc.port.KeycloakAuthPort;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class GoogleLoginAuthHandler implements CommandHandler<AuthGoogleLogin, GoogleAuthUrlDTO> {
    private final KeycloakAuthPort keycloakAuthPort;
    private final AuthMapper authMapper;

    @Override
    public GoogleAuthUrlDTO handle(AuthGoogleLogin command) {
        String state = UUID.randomUUID().toString();
        String method = command.getCodeChallengeMethod();
        if (method == null || method.isBlank()) {
            method = "S256";
        }
        return authMapper.toGoogleAuthUrlDTO(
                keycloakAuthPort.buildGoogleAuthorizationUrl(
                        command.getRedirectUri(),
                        command.getCodeChallenge(),
                        method,
                        state
                )
        );
    }

    @Override
    public Class<AuthGoogleLogin> commandType() {
        return AuthGoogleLogin.class;
    }
}
