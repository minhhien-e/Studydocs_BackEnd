package studydoc.command;

import lombok.Value;

@Value(staticConstructor = "commandOf")
public class DeleteUser implements UserCommand {
    String id;
}
