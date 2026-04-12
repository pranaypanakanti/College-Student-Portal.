package com.college.student.portal.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Value("${app.base-url}")
    private String baseUrl;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendPasswordResetEmail(String toEmail, String token) {

        String resetLink = baseUrl + "/api/auth/reset-password?token=" + token;

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(toEmail);
        message.setSubject("College Student Portal - Password Reset Request");
        message.setText(
                "Hello,\n\n"
                        + "You requested a password reset. Use the link below to reset your password:\n\n"
                        + resetLink + "\n\n"
                        + "Or use this token in Postman:\n"
                        + "Token: " + token + "\n\n"
                        + "This link expires in 30 minutes.\n\n"
                        + "If you didn't request this, please ignore this email.\n\n"
                        + "— College Student Portal Team"
        );

        mailSender.send(message);
    }
}