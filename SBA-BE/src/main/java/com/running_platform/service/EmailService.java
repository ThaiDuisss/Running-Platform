package com.running_platform.service;

public interface EmailService {
    void sendVerificationEmail(String email, String content, String subject);
}
