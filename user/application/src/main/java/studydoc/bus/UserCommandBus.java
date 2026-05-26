package studydoc.bus;

import studydoc.command.UserCommand;

public interface UserCommandBus {
    /**
     * Gửi command đến handler tương ứng.
     *
     * @param command Command cần xử lý.
     * @param <C> Kiểu command.
     * @param <R> Kiểu kết quả trả về (UserEntity, Boolean, v.v).
     * @return Kết quả xử lý command.
     * @throws com.error.exception.DomainException nếu có lỗi.
     */
    <C extends UserCommand, R> R send(C command);
}
