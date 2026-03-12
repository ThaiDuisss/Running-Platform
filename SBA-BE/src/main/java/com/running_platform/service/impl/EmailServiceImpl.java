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
    public void sendVerificationEmail(String email, String content, String subject) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject(subject);
        message.setText(content);

        mailSender.send(message);
    }
}
