package studydoc.handler.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import studydoc.command.auth.AuthRegister;
import studydoc.exception.KeycloakIntegrationException;
import studydoc.handler.CommandHandler;
import studydoc.port.KeycloakAdminPort;
import studydoc.repository.UserRepository;
import studydoc.service.UserDomainService;
import studydoc.vo.User;

@Component
@RequiredArgsConstructor
public class RegisterAuthHandler implements CommandHandler<AuthRegister, Void> {
    private final KeycloakAdminPort keycloakAdminPort;
    private final UserRepository userRepository;
    private final UserDomainService userDomainService;

    @Override
    public Void handle(AuthRegister command) {
        userDomainService.verifyUsernameUniqueness(command.getUsername());

        String keycloakId = null;
        try {
            keycloakId = keycloakAdminPort.createUser(
                    command.getUsername(),
                    command.getPassword(),
                    command.getFullName()
            );

            User user = User.createNewProfile(
                    keycloakId,
                    command.getUsername(),
                    command.getFullName()
            );
            userRepository.save(user);
            return null;
        } catch (Exception ex) {
            if (keycloakId != null) {
                try {
                    keycloakAdminPort.deleteUser(keycloakId);
                } catch (Exception rollbackEx) {
                    throw new KeycloakIntegrationException(
                            500,
                            5002,
                            "Register failed and rollback Keycloak user failed",
                            rollbackEx
                    );
                }
            }
            throw ex;
        }
    }

    @Override
    public Class<AuthRegister> commandType() {
        return AuthRegister.class;
    }
}
