package studydoc.mapper;

import org.springframework.stereotype.Component;
import studydoc.command.UpdateUser;
import studydoc.command.UpdateUserImage;
import studydoc.command.UpdateUserInfo;
import studydoc.request.UpdateRequest;
import studydoc.request.UpdateUserImageRequest;
import studydoc.request.UpdateUserInfoRequest;

@Component
public class RequestMapper {
    public UpdateUser toUpdateUserCommand(String id, UpdateRequest request) {
        return UpdateUser.commandOf(
                id,
                request.getFullName(),
                request.getPhoneNumber(),
                request.getAvatarUrl(),
                request.getGender(),
                request.getDateOfBirth(),
                request.getAddress(),
                request.getSchool(),
                request.isPrivate()
        );
    }

    public UpdateUserInfo toUpdateUserInfoCommand(String id, UpdateUserInfoRequest request) {
        return UpdateUserInfo.commandOf(
                id,
                request.getFullName(),
                request.getPhoneNumber(),
                request.getGender(),
                request.getDateOfBirth(),
                request.getAddress(),
                request.getSchool(),
                request.isPrivate()
        );
    }

    public UpdateUserImage toUpdateUserImageCommand(String id, UpdateUserImageRequest request) {
        return UpdateUserImage.commandOf(
                id,
                request.getAvatarUrl()
        );
    }
}
