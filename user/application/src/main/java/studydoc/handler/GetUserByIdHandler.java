package studydoc.handler;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import studydoc.command.GetUserById;
import studydoc.dto.UserDTO;
import studydoc.mapper.VoMapper;
import studydoc.repository.UserRepository;
import studydoc.vo.User;

@RequiredArgsConstructor
@Component
public class GetUserByIdHandler implements CommandHandler<GetUserById, UserDTO> {
    private final UserRepository userRepository;
    private final VoMapper voMapper;
    private final studydoc.integration.MediaIntegrationService mediaIntegrationService;

    @Override
    public UserDTO handle(GetUserById command) {
        User user = userRepository.findById(command.getId())
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy người dùng với ID: " + command.getId()));
        return voMapper.toUserDTO(user, user.getAvatarId() != null ? mediaIntegrationService.getMediaUrl(user.getAvatarId()) : user.getAvatarUrl());
    }

    @Override
    public Class<GetUserById> commandType() {
        return GetUserById.class;
    }
}
