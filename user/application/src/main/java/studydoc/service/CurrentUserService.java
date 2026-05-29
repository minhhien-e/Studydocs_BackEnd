package studydoc.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import studydoc.port.AuthenticatedUserAccessor;
import studydoc.port.KeycloakUserInfo;
import studydoc.port.TokenIdentityResolver;
import studydoc.repository.UserRepository;
import studydoc.vo.User;

@Service
@RequiredArgsConstructor
public class CurrentUserService {
    private final AuthenticatedUserAccessor authenticatedUserAccessor;
    private final TokenIdentityResolver tokenIdentityResolver;
    private final UserRepository userRepository;

    /**
     * Lấy profile MongoDB của user đang đăng nhập.
     * 1. Bearer token từ SecurityContext
     * 2. Keycloak userinfo → keycloakId
     * 3. MongoDB findByKeycloakId
     */
    public User getCurrentUser() {
        String accessToken = authenticatedUserAccessor.getBearerToken()
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy access token"));

        KeycloakUserInfo identity = tokenIdentityResolver.resolveFromAccessToken(accessToken);

        return userRepository.findByKeycloakId(identity.keycloakId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Không tìm thấy profile người dùng với keycloakId: " + identity.keycloakId()));
    }
}
