package studydoc.exception;

import lombok.Getter;

@Getter
public class KeycloakIntegrationException extends RuntimeException {
    private final int httpStatus;
    private final int errorCode;

    public KeycloakIntegrationException(int httpStatus, int errorCode, String message) {
        super(message);
        this.httpStatus = httpStatus;
        this.errorCode = errorCode;
    }

    public KeycloakIntegrationException(int httpStatus, int errorCode, String message, Throwable cause) {
        super(message, cause);
        this.httpStatus = httpStatus;
        this.errorCode = errorCode;
    }
}
