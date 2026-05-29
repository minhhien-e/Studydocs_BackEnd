package studydoc.command.auth;

import lombok.Value;
import studydoc.command.UserCommand;

@Value(staticConstructor = "commandOf")
public class AuthRefreshToken implements UserCommand {
    String refreshToken;
}
