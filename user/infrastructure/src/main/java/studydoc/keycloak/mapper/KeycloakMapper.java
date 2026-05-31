package studydoc.keycloak.mapper;

import org.springframework.stereotype.Component;
import studydoc.keycloak.dto.KeycloakAdminUserDto;
import studydoc.keycloak.dto.KeycloakTokenResponseDto;
import studydoc.keycloak.dto.KeycloakUserInfoDto;
import studydoc.port.KeycloakUserInfo;
import studydoc.port.KeycloakUserRepresentation;
import studydoc.port.TokenResponse;

@Component
public class KeycloakMapper {

    public TokenResponse toTokenResponse(KeycloakTokenResponseDto dto) {
        return new TokenResponse(dto.getAccessToken(), dto.getRefreshToken(), dto.getExpiresIn());
    }

    public KeycloakUserInfo toUserInfo(KeycloakUserInfoDto dto) {
        String fullName = dto.getName();
        if (fullName == null && dto.getGivenName() != null) {
            fullName = dto.getGivenName();
            if (dto.getFamilyName() != null) {
                fullName = fullName + " " + dto.getFamilyName();
            }
        }
        return new KeycloakUserInfo(
                dto.getSub(),
                dto.getPreferredUsername(),
                dto.getEmail(),
                fullName
        );
    }

    public KeycloakUserRepresentation toUserRepresentation(KeycloakAdminUserDto dto) {
        return new KeycloakUserRepresentation(
                dto.getId(),
                dto.getUsername(),
                dto.getEmail(),
                dto.getFirstName(),
                dto.getLastName()
        );
    }
}
