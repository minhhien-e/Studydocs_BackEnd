package studydoc.command;

import lombok.Value;

@Value(staticConstructor = "commandOf")
public class GetOtherUserInfo implements UserCommand {
    String id;
}
