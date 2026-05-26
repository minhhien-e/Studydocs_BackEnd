package studydoc.handler;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import studydoc.command.GetAllUsers;
import studydoc.dto.UserDTO;
import studydoc.mapper.VoMapper;
import studydoc.repository.UserRepository;

import java.util.List;

@RequiredArgsConstructor
@Component
public class GetAllUsersHandler implements CommandHandler<GetAllUsers, List<UserDTO>> {
    private final UserRepository userRepository;
    private final VoMapper voMapper;

    @Override
    public List<UserDTO> handle(GetAllUsers command) {
        return userRepository.findAll().stream()
                .map(voMapper::toUserDTO)
                .toList();
    }

    @Override
    public Class<GetAllUsers> commandType() {
        return GetAllUsers.class;
    }
}
