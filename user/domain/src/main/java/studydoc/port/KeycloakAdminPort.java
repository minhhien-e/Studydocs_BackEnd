package studydoc.port;

import java.util.Optional;

public interface KeycloakAdminPort {
    String createUser(String username, String password, String fullName);

    void assignDefaultRegistrationRole(String keycloakUserId);

    void deleteUser(String keycloakId);

    Optional<KeycloakUserRepresentation> findUserByUsername(String username);

    void sendResetPasswordEmail(String keycloakUserId);
}
