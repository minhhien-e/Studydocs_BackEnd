package studydoc.mapper;

import org.springframework.stereotype.Component;
import studydoc.command.RegisterUser;
import studydoc.vo.User;
@Component
public class CommandMapper {
    public User commandToUser(RegisterUser command){
        User user = new User(
                null, // ID sẽ được generate (UUID)
                command.getFullName(),
                command.getUsername(),
                command.getEmail(),
                command.getPhoneNumber(),
                command.getAvatarUrl(),
                command.getGender(),
                command.getDateOfBirth(),
                command.getAddress(),
                command.getSchool(),
                0, 0, 0, 0, 0
        );
        return user;
    }
}
