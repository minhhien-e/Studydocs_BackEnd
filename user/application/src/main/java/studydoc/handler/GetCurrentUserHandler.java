package studydoc.handler;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import studydoc.command.GetCurrentUser;
import studydoc.dto.UserDTO;
import studydoc.mapper.VoMapper;
import studydoc.service.CurrentUserService;
import studydoc.vo.User;

@RequiredArgsConstructor
@Component
public class GetCurrentUserHandler implements CommandHandler<GetCurrentUser, UserDTO> {
    private final CurrentUserService currentUserService;
    private final VoMapper voMapper;
    private final studydoc.integration.MediaIntegrationService mediaIntegrationService;

    @Override
    public UserDTO handle(GetCurrentUser command) {
        User user = currentUserService.getCurrentUser();
        String avatarUrl = user.getAvatarId() != null ? mediaIntegrationService.getMediaUrl(user.getAvatarId()) : null;
        return voMapper.toUserDTO(user, avatarUrl);
    }

    @Override
    public Class<GetCurrentUser> commandType() {
        return GetCurrentUser.class;
    }
}
