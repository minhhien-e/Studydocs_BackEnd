package studydoc.keycloak.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class KeycloakAdminUserDto {
    private String id;
    private String username;
    private String email;
    private String firstName;
    private String lastName;
}
