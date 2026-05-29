package studydoc.handler;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import studydoc.command.DeleteUser;
import studydoc.repository.UserRepository;

@RequiredArgsConstructor
@Component
public class DeleteUserHandler implements CommandHandler<DeleteUser, Boolean> {
    private final UserRepository userRepository;

    @Override
    public Boolean handle(DeleteUser command) {
        userRepository.findById(command.getId())
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy người dùng với ID: " + command.getId()));
        
        userRepository.deleteById(command.getId());
        return true;
    }

    @Override
    public Class<DeleteUser> commandType() {
        return DeleteUser.class;
    }
}
