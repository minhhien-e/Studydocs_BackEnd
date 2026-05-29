package studydoc.handler;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import studydoc.command.RegisterUser;
import studydoc.dto.UserDTO;
import studydoc.mapper.CommandMapper;
import studydoc.mapper.VoMapper;
import studydoc.repository.UserRepository;
import studydoc.service.UserDomainService;
import studydoc.vo.User;


@RequiredArgsConstructor
@Component
public class RegisterUserHandler implements CommandHandler<RegisterUser,UserDTO>{
    private final UserRepository userRepository;
    private final UserDomainService userDomainService;
    private final CommandMapper commandMapper;
    private final VoMapper voMapper;
    private final studydoc.integration.MediaIntegrationService mediaIntegrationService;
    @Override
    public UserDTO handle(RegisterUser command) {
        User user = commandMapper.commandToUser(command);
        userDomainService.verifyUserUniqueness(user);
        User savedUser = userRepository.save(user);
        return voMapper.toUserDTO(savedUser, savedUser.getAvatarMediaId() != null ? mediaIntegrationService.getMediaUrl(savedUser.getAvatarMediaId()) : null);
    }

    @Override
    public Class<RegisterUser> commandType() {
        return RegisterUser.class;
    }
}
