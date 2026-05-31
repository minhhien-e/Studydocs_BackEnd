package studydoc.port;

/**
 * Giải mã / resolve danh tính user từ access token qua Keycloak (userinfo endpoint).
 * Dùng chung cho /me, authorization, audit, v.v.
 */
public interface TokenIdentityResolver {
    KeycloakUserInfo resolveFromAccessToken(String accessToken);
}
