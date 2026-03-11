package com.running_platform.service.impl;

import com.running_platform.service.EmailService;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {
    private final JavaMailSenderImpl mailSender;

    public EmailServiceImpl(JavaMailSenderImpl mailSender) {
        this.mailSender = mailSender;
    }

    @Async
    public void sendVerificationEmail(String email, String token) {

        String link = "http://localhost:8080/auth/verify?token=" + token;

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Verify your account");
        message.setText("Click this link to verify: " + link);

        mailSender.send(message);
    }
}
