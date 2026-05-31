package studydoc.request.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthGoogleLoginRequest {
    @NotBlank(message = "Redirect URI không được để trống")
    private String redirectUri;

    @NotBlank(message = "Code challenge không được để trống")
    private String codeChallenge;

    private String codeChallengeMethod;
}
