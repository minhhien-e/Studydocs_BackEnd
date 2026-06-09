package studydoc.handler.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import studydoc.command.auth.AuthForgotPassword;
import studydoc.handler.CommandHandler;
import studydoc.port.KeycloakAdminPort;
import studydoc.port.KeycloakUserRepresentation;

@Component
@RequiredArgsConstructor
public class ForgotPasswordAuthHandler implements CommandHandler<AuthForgotPassword, Void> {
    private final KeycloakAdminPort keycloakAdminPort;

    @Override
    public Void handle(AuthForgotPassword command) {
        KeycloakUserRepresentation user = keycloakAdminPort.findUserByUsername(command.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy người dùng: " + command.getUsername()));

        if (!StringUtils.hasText(user.email())) {
            throw new IllegalArgumentException("Tài khoản chưa có email, không thể gửi email đặt lại mật khẩu");
        }

        keycloakAdminPort.sendResetPasswordEmail(user.id());
        return null;
    }

    @Override
    public Class<AuthForgotPassword> commandType() {
        return AuthForgotPassword.class;
    }
}
