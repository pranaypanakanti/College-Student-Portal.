package com.college.student.portal.service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.college.student.portal.dto.ForgotPasswordDTO;
import com.college.student.portal.dto.ResetPasswordDTO;
import com.college.student.portal.entity.Admin;
import com.college.student.portal.entity.Faculty;
import com.college.student.portal.entity.PasswordResetToken;
import com.college.student.portal.entity.Student;
import com.college.student.portal.repository.AdminRepository;
import com.college.student.portal.repository.FacultyRepository;
import com.college.student.portal.repository.PasswordResetTokenRepository;
import com.college.student.portal.repository.StudentRepository;

@Service
public class PasswordResetService {

    private final PasswordResetTokenRepository tokenRepository;
    private final StudentRepository studentRepository;
    private final FacultyRepository facultyRepository;
    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    public PasswordResetService(PasswordResetTokenRepository tokenRepository,
                                StudentRepository studentRepository, FacultyRepository facultyRepository,
                                AdminRepository adminRepository, PasswordEncoder passwordEncoder,
                                EmailService emailService) {
        this.tokenRepository = tokenRepository;
        this.studentRepository = studentRepository;
        this.facultyRepository = facultyRepository;
        this.adminRepository = adminRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
    }

    // FORGOT PASSWORD
    public ResponseEntity<Map<String, Object>> forgotPassword(ForgotPasswordDTO dto) {

        String email = dto.getEmail();

        boolean emailExists =
                studentRepository.findByEmail(email).isPresent() ||
                        facultyRepository.findByEmail(email).isPresent() ||
                        adminRepository.findByEmail(email).isPresent();

        if (!emailExists) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "No account found with this email!"));
        }

        String token = UUID.randomUUID().toString();

        PasswordResetToken resetToken = new PasswordResetToken();
        resetToken.setToken(token);
        resetToken.setEmail(email);
        resetToken.setExpiryDate(LocalDateTime.now().plusMinutes(30)); // 30 min expiry
        resetToken.setUsed(false);

        tokenRepository.save(resetToken);

        try {
            emailService.sendPasswordResetEmail(email, token);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(
                            "message", "Failed to send email. Token generated for testing.",
                            "token", token // Include token in response for Postman testing
                    ));
        }

        return ResponseEntity.ok(Map.of(
                "message", "Reset link sent to email!",
                "token", token // Include for Postman testing convenience
        ));
    }

    // RESET PASSWORD
    public ResponseEntity<Map<String, Object>> resetPassword(ResetPasswordDTO dto) {

        PasswordResetToken resetToken = tokenRepository.findByToken(dto.getToken())
                .orElse(null);

        if (resetToken == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", "Invalid reset token!"));
        }

        if (resetToken.isExpired()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", "Reset token has expired! Please request a new one."));
        }

        if (resetToken.isUsed()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", "Reset token has already been used!"));
        }

        if (dto.getNewPassword().length() < 8) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", "Password must be at least 8 characters!"));
        }

        String email = resetToken.getEmail();
        String encodedPassword = passwordEncoder.encode(dto.getNewPassword());
        boolean updated = false;

        Student student = studentRepository.findByEmail(email).orElse(null);
        if (student != null) {
            student.setPasswordHash(encodedPassword);
            studentRepository.save(student);
            updated = true;
        }

        Faculty faculty = facultyRepository.findByEmail(email).orElse(null);
        if (faculty != null) {
            faculty.setPasswordHash(encodedPassword);
            facultyRepository.save(faculty);
            updated = true;
        }

        Admin admin = adminRepository.findByEmail(email).orElse(null);
        if (admin != null) {
            admin.setPasswordHash(encodedPassword);
            adminRepository.save(admin);
            updated = true;
        }

        if (!updated) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "User not found!"));
        }

        resetToken.setUsed(true);
        tokenRepository.save(resetToken);

        return ResponseEntity.ok(Map.of("message", "Password reset successfully!"));
    }
}