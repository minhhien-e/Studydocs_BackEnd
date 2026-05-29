package studydoc.keycloak;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import studydoc.exception.KeycloakIntegrationException;
import studydoc.keycloak.config.KeycloakProperties;
import studydoc.keycloak.dto.KeycloakAdminUserDto;
import studydoc.keycloak.dto.KeycloakCreateUserRequestDto;
import studydoc.keycloak.dto.KeycloakCredentialDto;
import studydoc.keycloak.dto.KeycloakTokenResponseDto;
import studydoc.keycloak.dto.KeycloakUpdateUserRequestDto;
import studydoc.keycloak.mapper.KeycloakMapper;
import studydoc.port.KeycloakAdminPort;
import studydoc.port.KeycloakUserRepresentation;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class KeycloakAdminService implements KeycloakAdminPort {
    private final KeycloakApiClient apiClient;
    private final KeycloakProperties properties;
    private final KeycloakMapper mapper;

    @Override
    public String createUser(String username, String password, String fullName) {
        String adminToken = getAdminAccessToken();
        NameParts nameParts = resolveNameParts(username, fullName);
        KeycloakCreateUserRequestDto request = new KeycloakCreateUserRequestDto(
                username,
                true,
                true,
                nameParts.firstName(),
                nameParts.lastName(),
                username + "@studydocs.local",
                List.of(),
                List.of(new KeycloakCredentialDto("password", password, false))
        );

        ResponseEntity<Void> response = apiClient.postJsonForEntity(
                properties.adminUsersEndpoint(),
                adminToken,
                request
        );

        String location = response.getHeaders().getFirst("Location");
        if (!StringUtils.hasText(location)) {
            throw new KeycloakIntegrationException(500, 5001, "Keycloak did not return user location");
        }
        String keycloakUserId = location.substring(location.lastIndexOf('/') + 1);

        KeycloakUpdateUserRequestDto updateRequest = new KeycloakUpdateUserRequestDto();
        updateRequest.setEmailVerified(true);
        updateRequest.setRequiredActions(List.of());
        apiClient.putJsonNoContent(
                properties.adminUserEndpoint(keycloakUserId),
                adminToken,
                updateRequest
        );

        return keycloakUserId;
    }

    @Override
    public void deleteUser(String keycloakId) {
        String adminToken = getAdminAccessToken();
        apiClient.delete(properties.adminUserEndpoint(keycloakId), adminToken);
    }

    @Override
    public Optional<KeycloakUserRepresentation> findUserByUsername(String username) {
        String adminToken = getAdminAccessToken();
        KeycloakAdminUserDto[] users = apiClient.getJson(
                properties.adminUsersEndpoint() + "?username=" + username + "&exact=true",
                adminToken,
                KeycloakAdminUserDto[].class
        );
        if (users == null || users.length == 0) {
            return Optional.empty();
        }
        return Optional.of(mapper.toUserRepresentation(users[0]));
    }

    @Override
    public void sendResetPasswordEmail(String keycloakUserId) {
        String adminToken = getAdminAccessToken();
        apiClient.putJsonNoContent(
                properties.adminUserEndpoint(keycloakUserId) + "/execute-actions-email",
                adminToken,
                List.of("UPDATE_PASSWORD")
        );
    }

    private String getAdminAccessToken() {
        KeycloakTokenResponseDto token = apiClient.postForm(
                properties.tokenEndpoint(),
                apiClient.clientCredentialsForm(),
                KeycloakTokenResponseDto.class
        );
        return token.getAccessToken();
    }

    private NameParts resolveNameParts(String username, String fullName) {
        if (!StringUtils.hasText(fullName)) {
            return new NameParts(username, "User");
        }

        String trimmed = fullName.trim();
        int lastSpace = trimmed.lastIndexOf(' ');
        if (lastSpace <= 0 || lastSpace >= trimmed.length() - 1) {
            return new NameParts(trimmed, "User");
        }

        return new NameParts(
                trimmed.substring(0, lastSpace).trim(),
                trimmed.substring(lastSpace + 1).trim()
        );
    }

    private record NameParts(String firstName, String lastName) {
    }
}
