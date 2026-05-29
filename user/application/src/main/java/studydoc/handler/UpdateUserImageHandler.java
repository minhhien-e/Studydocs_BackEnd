package studydoc.handler;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import studydoc.command.UpdateUserImage;
import studydoc.dto.UserDTO;
import studydoc.mapper.VoMapper;
import studydoc.repository.UserRepository;
import studydoc.vo.User;

@RequiredArgsConstructor
@Component
public class UpdateUserImageHandler implements CommandHandler<UpdateUserImage, UserDTO> {
    private final UserRepository userRepository;
    private final VoMapper voMapper;
    private final MediaIntegrationService mediaIntegrationService;

    @Override
    public UserDTO handle(UpdateUserImage command) {
        User user = userRepository.findById(command.getId())
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy người dùng với ID: " + command.getId()));
        
        String avatarUrl = mediaIntegrationService.getMediaUrl(command.getMediaId());
        user.updateAvatar(avatarUrl);
        
        User savedUser = userRepository.save(user);
        return voMapper.toUserDTO(savedUser);
    }

    @Override
    public Class<UpdateUserImage> commandType() {
        return UpdateUserImage.class;
    }
}
