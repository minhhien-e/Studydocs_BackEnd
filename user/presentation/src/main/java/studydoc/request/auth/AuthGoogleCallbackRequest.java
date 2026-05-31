package studydoc.request.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthGoogleCallbackRequest {
    @NotBlank(message = "Authorization code không được để trống")
    private String code;

    @NotBlank(message = "Code verifier không được để trống")
    private String codeVerifier;

    @NotBlank(message = "Redirect URI không được để trống")
    private String redirectUri;
}
