package studydoc.handler;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import studydoc.command.GetOtherUserInfo;
import studydoc.dto.OtherUserInfoDTO;
import studydoc.mapper.VoMapper;
import studydoc.repository.UserRepository;
import studydoc.vo.User;

@RequiredArgsConstructor
@Component
public class GetOtherUserInfoHandler implements CommandHandler<GetOtherUserInfo, OtherUserInfoDTO> {
    private final UserRepository userRepository;
    private final VoMapper voMapper;

    @Override
    public OtherUserInfoDTO handle(GetOtherUserInfo command) {
        User user = userRepository.findById(command.getId())
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy người dùng với ID: " + command.getId()));
        return voMapper.toOtherUserInfoDTO(user);
    }

    @Override
    public Class<GetOtherUserInfo> commandType() {
        return GetOtherUserInfo.class;
    }
}
