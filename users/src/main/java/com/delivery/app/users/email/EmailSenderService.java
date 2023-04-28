package com.delivery.app.users.email;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailSenderService implements EmailSender{
    private final JavaMailSender javaMailSender;
    @Value("${spring.mail.username}")
    private String mailSender;

    @Override
    public void sendEmailVerification(String receiver, String email) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, "utf-8");
        mimeMessageHelper.setText(email , true);
        mimeMessageHelper.setTo(receiver);
        mimeMessageHelper.setSubject("Confirm your email");
        mimeMessageHelper.setFrom(mailSender);
        javaMailSender.send(mimeMessage);
    }
}
