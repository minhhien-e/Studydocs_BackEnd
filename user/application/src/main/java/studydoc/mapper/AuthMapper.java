package studydoc.mapper;

import org.springframework.stereotype.Component;
import studydoc.dto.auth.GoogleAuthUrlDTO;
import studydoc.dto.auth.TokenResponseDTO;
import studydoc.port.GoogleAuthUrl;
import studydoc.port.TokenResponse;

@Component
public class AuthMapper {
    public TokenResponseDTO toTokenResponseDTO(TokenResponse tokenResponse) {
        return new TokenResponseDTO(
                tokenResponse.accessToken(),
                tokenResponse.refreshToken(),
                tokenResponse.expiresIn()
        );
    }

    public GoogleAuthUrlDTO toGoogleAuthUrlDTO(GoogleAuthUrl googleAuthUrl) {
        return new GoogleAuthUrlDTO(
                googleAuthUrl.authorizationUrl(),
                googleAuthUrl.state()
        );
    }
}
