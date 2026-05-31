package studydoc.keycloak.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class KeycloakCredentialDto {
    private String type;
    private String value;
    private boolean temporary;
}
