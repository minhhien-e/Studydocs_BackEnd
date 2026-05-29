package studydoc.handler;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import studydoc.command.UpdateUserInfo;
import studydoc.dto.UserDTO;
import studydoc.mapper.VoMapper;
import studydoc.repository.UserRepository;
import studydoc.vo.User;

@RequiredArgsConstructor
@Component
public class UpdateUserInfoHandler implements CommandHandler<UpdateUserInfo, UserDTO> {
    private final UserRepository userRepository;
    private final VoMapper voMapper;
    private final studydoc.integration.MediaIntegrationService mediaIntegrationService;

    @Override
    public UserDTO handle(UpdateUserInfo command) {
        User user = userRepository.findById(command.getId())
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy người dùng với ID: " + command.getId()));
        
        user.updateInfo(
                command.getFullName(),
                command.getPhoneNumber(),
                command.getGender(),
                command.getDateOfBirth(),
                command.getAddress(),
                command.getSchool(),
                command.isPrivate()
        );
        
        User savedUser = userRepository.save(user);
        return voMapper.toUserDTO(savedUser, savedUser.getAvatarMediaId() != null ? mediaIntegrationService.getMediaUrl(savedUser.getAvatarMediaId()) : null);
    }

    @Override
    public Class<UpdateUserInfo> commandType() {
        return UpdateUserInfo.class;
    }
}
