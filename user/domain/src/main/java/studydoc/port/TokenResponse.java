package studydoc.port;

public record TokenResponse(String accessToken, String refreshToken, long expiresIn) {
}
