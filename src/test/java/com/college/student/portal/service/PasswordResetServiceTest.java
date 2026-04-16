package com.college.student.portal.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.college.student.portal.dto.ForgotPasswordDTO;
import com.college.student.portal.dto.ResetPasswordDTO;
import com.college.student.portal.entity.Admin;
import com.college.student.portal.entity.Faculty;
import com.college.student.portal.entity.PasswordResetToken;
import com.college.student.portal.entity.Student;
import com.college.student.portal.repository.*;

@ExtendWith(MockitoExtension.class)
class PasswordResetServiceTest {

    @Mock private PasswordResetTokenRepository tokenRepository;
    @Mock private StudentRepository studentRepository;
    @Mock private FacultyRepository facultyRepository;
    @Mock private AdminRepository adminRepository;
    @Mock private PasswordEncoder passwordEncoder;
    @Mock private EmailService emailService;

    @InjectMocks private PasswordResetService passwordResetService;

    // ========== FORGOT PASSWORD TESTS ==========

    @Test
    void testForgotPassword_StudentEmailExists() {
        ForgotPasswordDTO dto = new ForgotPasswordDTO("student@test.com");

        when(studentRepository.findByEmail("student@test.com")).thenReturn(Optional.of(new Student()));
        when(tokenRepository.save(any())).thenReturn(new PasswordResetToken());
        doNothing().when(emailService).sendPasswordResetEmail(any(), any());

        ResponseEntity<?> response = passwordResetService.forgotPassword(dto);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(tokenRepository).save(any(PasswordResetToken.class));
    }

    @Test
    void testForgotPassword_FacultyEmailExists() {
        ForgotPasswordDTO dto = new ForgotPasswordDTO("faculty@test.com");

        when(studentRepository.findByEmail("faculty@test.com")).thenReturn(Optional.empty());
        when(facultyRepository.findByEmail("faculty@test.com")).thenReturn(Optional.of(new Faculty()));
        when(tokenRepository.save(any())).thenReturn(new PasswordResetToken());
        doNothing().when(emailService).sendPasswordResetEmail(any(), any());

        ResponseEntity<?> response = passwordResetService.forgotPassword(dto);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testForgotPassword_AdminEmailExists() {
        ForgotPasswordDTO dto = new ForgotPasswordDTO("admin@test.com");

        when(studentRepository.findByEmail("admin@test.com")).thenReturn(Optional.empty());
        when(facultyRepository.findByEmail("admin@test.com")).thenReturn(Optional.empty());
        when(adminRepository.findByEmail("admin@test.com")).thenReturn(Optional.of(new Admin()));
        when(tokenRepository.save(any())).thenReturn(new PasswordResetToken());
        doNothing().when(emailService).sendPasswordResetEmail(any(), any());

        ResponseEntity<?> response = passwordResetService.forgotPassword(dto);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testForgotPassword_EmailNotFound() {
        ForgotPasswordDTO dto = new ForgotPasswordDTO("unknown@test.com");

        when(studentRepository.findByEmail(any())).thenReturn(Optional.empty());
        when(facultyRepository.findByEmail(any())).thenReturn(Optional.empty());
        when(adminRepository.findByEmail(any())).thenReturn(Optional.empty());

        ResponseEntity<?> response = passwordResetService.forgotPassword(dto);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testForgotPassword_EmailSendFails() {
        ForgotPasswordDTO dto = new ForgotPasswordDTO("student@test.com");

        when(studentRepository.findByEmail("student@test.com")).thenReturn(Optional.of(new Student()));
        when(tokenRepository.save(any())).thenReturn(new PasswordResetToken());
        doThrow(new RuntimeException("SMTP error")).when(emailService).sendPasswordResetEmail(any(), any());

        ResponseEntity<?> response = passwordResetService.forgotPassword(dto);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    // ========== RESET PASSWORD TESTS ==========

    @Test
    void testResetPassword_InvalidToken() {
        ResetPasswordDTO dto = new ResetPasswordDTO("bad-token", "newpass123");

        when(tokenRepository.findByToken("bad-token")).thenReturn(Optional.empty());

        ResponseEntity<?> response = passwordResetService.resetPassword(dto);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void testResetPassword_ExpiredToken() {
        PasswordResetToken token = new PasswordResetToken();
        token.setToken("expired-token");
        token.setEmail("test@test.com");
        token.setExpiryDate(LocalDateTime.now().minusHours(1));
        token.setUsed(false);

        when(tokenRepository.findByToken("expired-token")).thenReturn(Optional.of(token));

        ResponseEntity<?> response = passwordResetService.resetPassword(
                new ResetPasswordDTO("expired-token", "newpass123"));
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void testResetPassword_AlreadyUsedToken() {
        PasswordResetToken token = new PasswordResetToken();
        token.setToken("used-token");
        token.setEmail("test@test.com");
        token.setExpiryDate(LocalDateTime.now().plusMinutes(30));
        token.setUsed(true);

        when(tokenRepository.findByToken("used-token")).thenReturn(Optional.of(token));

        ResponseEntity<?> response = passwordResetService.resetPassword(
                new ResetPasswordDTO("used-token", "newpass123"));
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void testResetPassword_WeakPassword() {
        PasswordResetToken token = new PasswordResetToken();
        token.setToken("valid-token");
        token.setEmail("test@test.com");
        token.setExpiryDate(LocalDateTime.now().plusMinutes(30));
        token.setUsed(false);

        when(tokenRepository.findByToken("valid-token")).thenReturn(Optional.of(token));

        ResponseEntity<?> response = passwordResetService.resetPassword(
                new ResetPasswordDTO("valid-token", "short"));
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void testResetPassword_Success_Student() {
        PasswordResetToken token = new PasswordResetToken();
        token.setToken("valid-token");
        token.setEmail("student@test.com");
        token.setExpiryDate(LocalDateTime.now().plusMinutes(30));
        token.setUsed(false);

        Student student = new Student();
        student.setEmail("student@test.com");

        when(tokenRepository.findByToken("valid-token")).thenReturn(Optional.of(token));
        when(passwordEncoder.encode(any())).thenReturn("new-hash");
        when(studentRepository.findByEmail("student@test.com")).thenReturn(Optional.of(student));
        when(facultyRepository.findByEmail("student@test.com")).thenReturn(Optional.empty());
        when(adminRepository.findByEmail("student@test.com")).thenReturn(Optional.empty());

        ResponseEntity<?> response = passwordResetService.resetPassword(
                new ResetPasswordDTO("valid-token", "newpassword123"));
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(studentRepository).save(any());
        verify(tokenRepository).save(any());
    }

    @Test
    void testResetPassword_Success_Faculty() {
        PasswordResetToken token = new PasswordResetToken();
        token.setToken("valid-token");
        token.setEmail("faculty@test.com");
        token.setExpiryDate(LocalDateTime.now().plusMinutes(30));
        token.setUsed(false);

        Faculty faculty = new Faculty();
        faculty.setEmail("faculty@test.com");

        when(tokenRepository.findByToken("valid-token")).thenReturn(Optional.of(token));
        when(passwordEncoder.encode(any())).thenReturn("new-hash");
        when(studentRepository.findByEmail("faculty@test.com")).thenReturn(Optional.empty());
        when(facultyRepository.findByEmail("faculty@test.com")).thenReturn(Optional.of(faculty));
        when(adminRepository.findByEmail("faculty@test.com")).thenReturn(Optional.empty());

        ResponseEntity<?> response = passwordResetService.resetPassword(
                new ResetPasswordDTO("valid-token", "newpassword123"));
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(facultyRepository).save(any());
    }

    @Test
    void testResetPassword_UserNotFoundInAnyTable() {
        PasswordResetToken token = new PasswordResetToken();
        token.setToken("valid-token");
        token.setEmail("ghost@test.com");
        token.setExpiryDate(LocalDateTime.now().plusMinutes(30));
        token.setUsed(false);

        when(tokenRepository.findByToken("valid-token")).thenReturn(Optional.of(token));
        when(passwordEncoder.encode(any())).thenReturn("new-hash");
        when(studentRepository.findByEmail("ghost@test.com")).thenReturn(Optional.empty());
        when(facultyRepository.findByEmail("ghost@test.com")).thenReturn(Optional.empty());
        when(adminRepository.findByEmail("ghost@test.com")).thenReturn(Optional.empty());

        ResponseEntity<?> response = passwordResetService.resetPassword(
                new ResetPasswordDTO("valid-token", "newpassword123"));
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}