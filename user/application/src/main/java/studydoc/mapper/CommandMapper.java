package studydoc.mapper;

import org.springframework.stereotype.Component;
import studydoc.command.RegisterUser;
import studydoc.vo.User;
@Component
public class CommandMapper {
    public User commandToUser(RegisterUser command){
        User user = new User(
                null, // ID sẽ được generate (UUID)
                command.getFullName(), // fullName
                command.getUsername(),
                command.getPassword(),
                command.getEmail(),
                null, // phoneNumber
                null, // avatarUrl
                null, // gender
                null, // dateOfBirth
                null, // address
                null, // school
                0, 0, 0, 0, 0
        );
        return user;
    }
}
