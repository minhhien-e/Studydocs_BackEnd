package studydoc.keycloak;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;
import studydoc.exception.KeycloakIntegrationException;
import studydoc.keycloak.config.KeycloakProperties;

import java.nio.charset.StandardCharsets;

@Component
@RequiredArgsConstructor
public class KeycloakApiClient {
    private static final Logger LOG = LoggerFactory.getLogger(KeycloakApiClient.class);

    private final RestClient keycloakRestClient;
    private final KeycloakProperties properties;

    public void postFormNoContent(String url, MultiValueMap<String, String> form) {
        LOG.info("Keycloak request POST form no-content url={}", sanitizeUrl(url));
        execute(() -> {
            keycloakRestClient.post()
                    .uri(url)
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .body(form)
                    .retrieve()
                    .onStatus(HttpStatusCode::isError, (request, response) -> {
                        String body = new String(response.getBody().readAllBytes(), StandardCharsets.UTF_8);
                        LOG.warn("Keycloak error response status={} url={} body={}",
                                response.getStatusCode().value(), sanitizeUrl(url), truncate(body));
                        throw mapHttpError(response.getStatusCode().value(), body);
                    })
                    .toBodilessEntity();
            return null;
        });
    }

    public <T> T postForm(String url, MultiValueMap<String, String> form, Class<T> responseType) {
        LOG.info("Keycloak request POST form url={}", sanitizeUrl(url));
        return execute(() -> keycloakRestClient.post()
                .uri(url)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(form)
                .retrieve()
                .onStatus(HttpStatusCode::isError, (request, response) -> {
                    String body = new String(response.getBody().readAllBytes(), StandardCharsets.UTF_8);
                    LOG.warn("Keycloak error response status={} url={} body={}",
                            response.getStatusCode().value(), sanitizeUrl(url), truncate(body));
                    throw mapHttpError(response.getStatusCode().value(), body);
                })
                .body(responseType));
    }

    public <T> T postJson(String url, String bearerToken, Object body, Class<T> responseType) {
        LOG.info("Keycloak request POST json url={}", sanitizeUrl(url));
        return execute(() -> keycloakRestClient.post()
                .uri(url)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + bearerToken)
                .contentType(MediaType.APPLICATION_JSON)
                .body(body)
                .retrieve()
                .onStatus(HttpStatusCode::isError, (request, response) -> {
                    String responseBody = new String(response.getBody().readAllBytes(), StandardCharsets.UTF_8);
                    LOG.warn("Keycloak error response status={} url={} body={}",
                            response.getStatusCode().value(), sanitizeUrl(url), truncate(responseBody));
                    throw mapHttpError(response.getStatusCode().value(), responseBody);
                })
                .body(responseType));
    }

    public void postJsonNoContent(String url, String bearerToken, Object body) {
        LOG.info("Keycloak request POST json no-content url={}", sanitizeUrl(url));
        execute(() -> {
            keycloakRestClient.post()
                    .uri(url)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + bearerToken)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(body)
                    .retrieve()
                    .onStatus(HttpStatusCode::isError, (request, response) -> {
                        String responseBody = new String(response.getBody().readAllBytes(), StandardCharsets.UTF_8);
                        LOG.warn("Keycloak error response status={} url={} body={}",
                                response.getStatusCode().value(), sanitizeUrl(url), truncate(responseBody));
                        throw mapHttpError(response.getStatusCode().value(), responseBody);
                    })
                    .toBodilessEntity();
            return null;
        });
    }

    public void putJsonNoContent(String url, String bearerToken, Object body) {
        LOG.info("Keycloak request PUT json no-content url={}", sanitizeUrl(url));
        execute(() -> {
            keycloakRestClient.put()
                    .uri(url)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + bearerToken)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(body)
                    .retrieve()
                    .onStatus(HttpStatusCode::isError, (request, response) -> {
                        String responseBody = new String(response.getBody().readAllBytes(), StandardCharsets.UTF_8);
                        LOG.warn("Keycloak error response status={} url={} body={}",
                                response.getStatusCode().value(), sanitizeUrl(url), truncate(responseBody));
                        throw mapHttpError(response.getStatusCode().value(), responseBody);
                    })
                    .toBodilessEntity();
            return null;
        });
    }

    public org.springframework.http.ResponseEntity<Void> postJsonForEntity(String url, String bearerToken, Object body) {
        LOG.info("Keycloak request POST json entity url={}", sanitizeUrl(url));
        return execute(() -> keycloakRestClient.post()
                .uri(url)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + bearerToken)
                .contentType(MediaType.APPLICATION_JSON)
                .body(body)
                .retrieve()
                .onStatus(HttpStatusCode::isError, (request, response) -> {
                    String responseBody = new String(response.getBody().readAllBytes(), StandardCharsets.UTF_8);
                    LOG.warn("Keycloak error response status={} url={} body={}",
                            response.getStatusCode().value(), sanitizeUrl(url), truncate(responseBody));
                    throw mapHttpError(response.getStatusCode().value(), responseBody);
                })
                .toBodilessEntity());
    }

    public <T> T getJson(String url, String bearerToken, Class<T> responseType) {
        LOG.info("Keycloak request GET json url={}", sanitizeUrl(url));
        return execute(() -> keycloakRestClient.get()
                .uri(url)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + bearerToken)
                .retrieve()
                .onStatus(HttpStatusCode::isError, (request, response) -> {
                    String responseBody = new String(response.getBody().readAllBytes(), StandardCharsets.UTF_8);
                    LOG.warn("Keycloak error response status={} url={} body={}",
                            response.getStatusCode().value(), sanitizeUrl(url), truncate(responseBody));
                    throw mapHttpError(response.getStatusCode().value(), responseBody);
                })
                .body(responseType));
    }

    public void delete(String url, String bearerToken) {
        LOG.info("Keycloak request DELETE url={}", sanitizeUrl(url));
        execute(() -> {
            keycloakRestClient.delete()
                    .uri(url)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + bearerToken)
                    .retrieve()
                    .onStatus(HttpStatusCode::isError, (request, response) -> {
                        String responseBody = new String(response.getBody().readAllBytes(), StandardCharsets.UTF_8);
                        LOG.warn("Keycloak error response status={} url={} body={}",
                                response.getStatusCode().value(), sanitizeUrl(url), truncate(responseBody));
                        throw mapHttpError(response.getStatusCode().value(), responseBody);
                    })
                    .toBodilessEntity();
            return null;
        });
    }

    public MultiValueMap<String, String> clientCredentialsForm() {
        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("grant_type", "client_credentials");
        form.add("client_id", properties.getClientId());
        form.add("client_secret", properties.getClientSecret());
        return form;
    }

    public MultiValueMap<String, String> passwordGrantForm(String username, String password) {
        MultiValueMap<String, String> form = clientAuthForm("password");
        form.add("username", username);
        form.add("password", password);
        form.add("scope", "openid offline_access");
        return form;
    }

    public MultiValueMap<String, String> refreshTokenForm(String refreshToken) {
        MultiValueMap<String, String> form = clientAuthForm("refresh_token");
        form.add("refresh_token", refreshToken);
        return form;
    }

    public MultiValueMap<String, String> authorizationCodeForm(String code, String redirectUri, String codeVerifier) {
        MultiValueMap<String, String> form = clientAuthForm("authorization_code");
        form.add("code", code);
        form.add("redirect_uri", redirectUri);
        form.add("code_verifier", codeVerifier);
        return form;
    }

    public MultiValueMap<String, String> logoutForm(String refreshToken) {
        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("client_id", properties.getClientId());
        form.add("client_secret", properties.getClientSecret());
        form.add("refresh_token", refreshToken);
        return form;
    }

    private MultiValueMap<String, String> clientAuthForm(String grantType) {
        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("grant_type", grantType);
        form.add("client_id", properties.getClientId());
        form.add("client_secret", properties.getClientSecret());
        return form;
    }

    private <T> T execute(SupplierWithException<T> supplier) {
        try {
            return supplier.get();
        } catch (KeycloakIntegrationException ex) {
            throw ex;
        } catch (Exception ex) {
            LOG.error("Keycloak unexpected error", ex);
            throw new KeycloakIntegrationException(500, 5000, "Keycloak service unavailable", ex);
        }
    }

    private KeycloakIntegrationException mapHttpError(int status, String body) {
        int errorCode = switch (status) {
            case 400 -> 4000;
            case 401 -> 4010;
            case 403 -> 4030;
            case 404 -> 4040;
            case 409 -> 4090;
            default -> 5000;
        };
        String message = switch (status) {
            case 401 -> "Invalid credentials or token";
            case 409 -> "User already exists";
            case 404 -> "Keycloak resource not found";
            default -> "Keycloak request failed";
        };
        if (body != null && body.contains("invalid_grant")) {
            message = body.contains("Account is not fully set up")
                    ? "Account is not fully set up"
                    : "Invalid credentials or token";
        }
        return new KeycloakIntegrationException(status, errorCode, message);
    }

    private String sanitizeUrl(String url) {
        return url == null ? "" : url.replaceAll("client_secret=[^&]+", "client_secret=***");
    }

    private String truncate(String body) {
        if (body == null) {
            return "";
        }
        return body.length() > 500 ? body.substring(0, 500) + "..." : body;
    }

    @FunctionalInterface
    private interface SupplierWithException<T> {
        T get() throws Exception;
    }
}
