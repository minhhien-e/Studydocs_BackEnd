package studydoc.command.auth;

import lombok.Value;
import studydoc.command.UserCommand;

@Value(staticConstructor = "commandOf")
public class AuthGoogleCallback implements UserCommand {
    String code;
    String codeVerifier;
    String redirectUri;
}
