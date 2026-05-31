package studydoc.handler.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import studydoc.command.auth.AuthLogin;
import studydoc.dto.auth.TokenResponseDTO;
import studydoc.handler.CommandHandler;
import studydoc.mapper.AuthMapper;
import studydoc.port.KeycloakAuthPort;

@Component
@RequiredArgsConstructor
public class LoginAuthHandler implements CommandHandler<AuthLogin, TokenResponseDTO> {
    private final KeycloakAuthPort keycloakAuthPort;
    private final AuthMapper authMapper;

    @Override
    public TokenResponseDTO handle(AuthLogin command) {
        return authMapper.toTokenResponseDTO(
                keycloakAuthPort.login(command.getUsername(), command.getPassword())
        );
    }

    @Override
    public Class<AuthLogin> commandType() {
        return AuthLogin.class;
    }
}
