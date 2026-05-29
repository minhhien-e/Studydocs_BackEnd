package studydoc.port;

public interface KeycloakAuthPort {
    TokenResponse login(String username, String password);

    TokenResponse refreshToken(String refreshToken);

    void logout(String refreshToken);

    GoogleAuthUrl buildGoogleAuthorizationUrl(
            String redirectUri,
            String codeChallenge,
            String codeChallengeMethod,
            String state
    );

    TokenResponse exchangeAuthorizationCode(String code, String redirectUri, String codeVerifier);

    KeycloakUserInfo getUserInfo(String accessToken);
}
