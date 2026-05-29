package studydoc.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GoogleAuthUrlDTO {
    private String authorizationUrl;
    private String state;
}
