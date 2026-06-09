package studydoc.handler.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import studydoc.command.auth.AuthRefreshToken;
import studydoc.dto.auth.TokenResponseDTO;
import studydoc.handler.CommandHandler;
import studydoc.mapper.AuthMapper;
import studydoc.port.KeycloakAuthPort;

@Component
@RequiredArgsConstructor
public class RefreshTokenAuthHandler implements CommandHandler<AuthRefreshToken, TokenResponseDTO> {
    private final KeycloakAuthPort keycloakAuthPort;
    private final AuthMapper authMapper;

    @Override
    public TokenResponseDTO handle(AuthRefreshToken command) {
        return authMapper.toTokenResponseDTO(
                keycloakAuthPort.refreshToken(command.getRefreshToken())
        );
    }

    @Override
    public Class<AuthRefreshToken> commandType() {
        return AuthRefreshToken.class;
    }
}
