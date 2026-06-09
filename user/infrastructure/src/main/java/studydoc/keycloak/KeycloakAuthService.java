package studydoc.keycloak;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;
import studydoc.keycloak.config.KeycloakProperties;
import studydoc.keycloak.dto.KeycloakTokenResponseDto;
import studydoc.keycloak.dto.KeycloakUserInfoDto;
import studydoc.keycloak.mapper.KeycloakMapper;
import studydoc.port.GoogleAuthUrl;
import studydoc.port.KeycloakAuthPort;
import studydoc.port.KeycloakUserInfo;
import studydoc.port.TokenResponse;

import java.nio.charset.StandardCharsets;

@Service
@RequiredArgsConstructor
public class KeycloakAuthService implements KeycloakAuthPort {
    private final KeycloakApiClient apiClient;
    private final KeycloakProperties properties;
    private final KeycloakMapper mapper;

    @Override
    public TokenResponse login(String username, String password) {
        KeycloakTokenResponseDto response = apiClient.postForm(
                properties.tokenEndpoint(),
                apiClient.passwordGrantForm(username, password),
                KeycloakTokenResponseDto.class
        );
        return mapper.toTokenResponse(response);
    }

    @Override
    public TokenResponse refreshToken(String refreshToken) {
        KeycloakTokenResponseDto response = apiClient.postForm(
                properties.tokenEndpoint(),
                apiClient.refreshTokenForm(refreshToken),
                KeycloakTokenResponseDto.class
        );
        return mapper.toTokenResponse(response);
    }

    @Override
    public void logout(String refreshToken) {
        apiClient.postFormNoContent(
                properties.logoutEndpoint(),
                apiClient.logoutForm(refreshToken)
        );
    }

    @Override
    public GoogleAuthUrl buildGoogleAuthorizationUrl(
            String redirectUri,
            String codeChallenge,
            String codeChallengeMethod,
            String state
    ) {
        String authorizationUrl = UriComponentsBuilder
                .fromUriString(properties.authorizationEndpoint())
                .queryParam("client_id", properties.getClientId())
                .queryParam("redirect_uri", redirectUri)
                .queryParam("response_type", "code")
                .queryParam("scope", "openid offline_access profile email")
                .queryParam("kc_idp_hint", "google")
                .queryParam("code_challenge", codeChallenge)
                .queryParam("code_challenge_method", codeChallengeMethod)
                .queryParam("state", state)
                .encode(StandardCharsets.UTF_8)
                .build()
                .toUriString();
        return new GoogleAuthUrl(authorizationUrl, state);
    }

    @Override
    public TokenResponse exchangeAuthorizationCode(String code, String redirectUri, String codeVerifier) {
        KeycloakTokenResponseDto response = apiClient.postForm(
                properties.tokenEndpoint(),
                apiClient.authorizationCodeForm(code, redirectUri, codeVerifier),
                KeycloakTokenResponseDto.class
        );
        return mapper.toTokenResponse(response);
    }

    @Override
    public KeycloakUserInfo getUserInfo(String accessToken) {
        KeycloakUserInfoDto response = apiClient.getJson(
                properties.userInfoEndpoint(),
                accessToken,
                KeycloakUserInfoDto.class
        );
        return mapper.toUserInfo(response);
    }
}
