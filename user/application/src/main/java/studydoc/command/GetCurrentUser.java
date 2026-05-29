package studydoc.command;

import lombok.Value;

@Value(staticConstructor = "commandOf")
public class GetCurrentUser implements UserCommand {
}
