package studydoc.handler;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import studydoc.command.GetCurrentUser;
import studydoc.dto.UserDTO;
import studydoc.mapper.VoMapper;
import studydoc.service.CurrentUserService;

@RequiredArgsConstructor
@Component
public class GetCurrentUserHandler implements CommandHandler<GetCurrentUser, UserDTO> {
    private final CurrentUserService currentUserService;
    private final VoMapper voMapper;

    @Override
    public UserDTO handle(GetCurrentUser command) {
        return voMapper.toUserDTO(currentUserService.getCurrentUser());
    }

    @Override
    public Class<GetCurrentUser> commandType() {
        return GetCurrentUser.class;
    }
}
