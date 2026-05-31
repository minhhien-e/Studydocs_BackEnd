package studydoc.handler.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import studydoc.command.auth.AuthLogout;
import studydoc.handler.CommandHandler;
import studydoc.port.KeycloakAuthPort;

@Component
@RequiredArgsConstructor
public class LogoutAuthHandler implements CommandHandler<AuthLogout, Void> {
    private final KeycloakAuthPort keycloakAuthPort;

    @Override
    public Void handle(AuthLogout command) {
        keycloakAuthPort.logout(command.getRefreshToken());
        return null;
    }

    @Override
    public Class<AuthLogout> commandType() {
        return AuthLogout.class;
    }
}
