package studydoc.handler;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import studydoc.command.LoginUser;
import studydoc.dto.UserDTO;
import studydoc.mapper.VoMapper;
import studydoc.repository.UserRepository;
import studydoc.vo.User;

@RequiredArgsConstructor
@Component
public class LoginUserHandler implements CommandHandler<LoginUser, UserDTO> {
    private final UserRepository userRepository;
    private final VoMapper voMapper;
    private final studydoc.integration.MediaIntegrationService mediaIntegrationService;

    @Override
    public UserDTO handle(LoginUser command) {
        User user = userRepository.findByUsername(command.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("Username hoặc password không đúng"));

        if (!user.getPassword().equals(command.getPassword())) {
            throw new IllegalArgumentException("Username hoặc password không đúng");
        }

        return voMapper.toUserDTO(user, user.getAvatarId() != null ? mediaIntegrationService.getMediaUrl(user.getAvatarId()) : user.getAvatarUrl());
    }

    @Override
    public Class<LoginUser> commandType() {
        return LoginUser.class;
    }
}
