package org.example.but_eo.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailService {

    private final JavaMailSender mailSender;

    public String sendVerificationCode(String toEmail) {
        String code = createCode();
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("[But_Eo] 이메일 인증 코드");
        message.setText("인증 코드: " + code);
        mailSender.send(message);
        return code;
    }

    private String createCode() {
        return String.valueOf((int) (Math.random() * 900000) + 100000);
    }
}