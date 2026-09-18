package com.studydocs.modules.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailService {

    private final JavaMailSender mailSender;

    public void sendPasswordResetToken(String toEmail, String token) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("Mã xác nhận khôi phục mật khẩu - StudyDocs");
        message.setText("Chào bạn,\n\n" +
                "Bạn đã yêu cầu khôi phục mật khẩu. Mã xác nhận của bạn là:\n\n" +
                token + "\n\n" +
                "Mã này sẽ hết hạn sau 15 phút. Nếu bạn không yêu cầu thay đổi mật khẩu, vui lòng bỏ qua email này.\n\n" +
                "Trân trọng,\nĐội ngũ StudyDocs");
        
        mailSender.send(message);
    }
}
