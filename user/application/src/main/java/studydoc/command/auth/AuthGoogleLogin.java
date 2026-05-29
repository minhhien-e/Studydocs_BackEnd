package studydoc.command.auth;

import lombok.Value;
import studydoc.command.UserCommand;

@Value(staticConstructor = "commandOf")
public class AuthGoogleLogin implements UserCommand {
    String redirectUri;
    String codeChallenge;
    String codeChallengeMethod;
}
