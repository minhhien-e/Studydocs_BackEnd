package studydoc.mapper;

import org.springframework.stereotype.Component;
import studydoc.command.RegisterUser;
import studydoc.request.RegisterRequest;
import studydoc.command.UpdateUser;
import studydoc.request.UpdateRequest;
import studydoc.command.UpdateUserInfo;
import studydoc.request.UpdateUserInfoRequest;
import studydoc.command.UpdateUserImage;
import studydoc.request.UpdateUserImageRequest;

@Component
public class RequestMapper {
    public RegisterUser toRegisterUserCommand(RegisterRequest request) {
        return RegisterUser.commandOf(
                request.getFullName(),
                request.getUsername(),
                request.getEmail(),
                request.getPhoneNumber(),
                request.getAvatarUrl(),
                request.getGender(),
                request.getDateOfBirth(),
                request.getAddress(),
                request.getSchool()
        );
    }

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
