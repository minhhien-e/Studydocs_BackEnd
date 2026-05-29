package studydoc.keycloak;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import studydoc.port.KeycloakAuthPort;
import studydoc.port.KeycloakUserInfo;
import studydoc.port.TokenIdentityResolver;

@Service
@RequiredArgsConstructor
public class KeycloakTokenIdentityResolver implements TokenIdentityResolver {
    private final KeycloakAuthPort keycloakAuthPort;

    @Override
    public KeycloakUserInfo resolveFromAccessToken(String accessToken) {
        return keycloakAuthPort.getUserInfo(accessToken);
    }
}
