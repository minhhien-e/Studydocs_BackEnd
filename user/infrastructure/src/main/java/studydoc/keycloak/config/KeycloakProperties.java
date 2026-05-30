package studydoc.keycloak.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "keycloak")
public class KeycloakProperties {
    private String serverUrl;
    private String realm;
    private String clientId;
    private String clientSecret;
    private String defaultRegistrationRole = "CUSTOMER";

    public String realmBasePath() {
        return serverUrl + "/realms/" + realm;
    }

    public String tokenEndpoint() {
        return realmBasePath() + "/protocol/openid-connect/token";
    }

    public String logoutEndpoint() {
        return realmBasePath() + "/protocol/openid-connect/logout";
    }

    public String authorizationEndpoint() {
        return realmBasePath() + "/protocol/openid-connect/auth";
    }

    public String userInfoEndpoint() {
        return realmBasePath() + "/protocol/openid-connect/userinfo";
    }

    public String adminUsersEndpoint() {
        return serverUrl + "/admin/realms/" + realm + "/users";
    }

    public String adminUserEndpoint(String keycloakUserId) {
        return adminUsersEndpoint() + "/" + keycloakUserId;
    }

    public String adminRealmRoleEndpoint(String roleName) {
        return serverUrl + "/admin/realms/" + realm + "/roles/" + roleName;
    }

    public String adminUserRealmRoleMappingsEndpoint(String keycloakUserId) {
        return adminUserEndpoint(keycloakUserId) + "/role-mappings/realm";
    }
}
