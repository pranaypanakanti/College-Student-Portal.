package com.college.student.portal.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.college.student.portal.dto.ForgotPasswordDTO;
import com.college.student.portal.dto.ResetPasswordDTO;
import com.college.student.portal.service.PasswordResetService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final PasswordResetService passwordResetService;

    public AuthController(PasswordResetService passwordResetService) {
        this.passwordResetService = passwordResetService;
    }

    // Forgot Password
    @PostMapping("/forgot-password")
    public ResponseEntity<Map<String, Object>> forgotPassword(
            @Valid @RequestBody ForgotPasswordDTO dto) {
        return passwordResetService.forgotPassword(dto);
    }

    // Reset Password
    @PostMapping("/reset-password")
    public ResponseEntity<Map<String, Object>> resetPassword(
            @Valid @RequestBody ResetPasswordDTO dto) {
        return passwordResetService.resetPassword(dto);
    }

    // Health Check
    @GetMapping("/health-check")
    public String healthCheck() {
        return "Positive";
    }
}