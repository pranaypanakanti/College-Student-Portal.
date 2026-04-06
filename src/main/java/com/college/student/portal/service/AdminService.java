package com.college.student.portal.service;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.college.student.portal.dto.*;
import com.college.student.portal.entity.Admin;
import com.college.student.portal.enums.Role;
import com.college.student.portal.repository.AdminRepository;
import com.college.student.portal.security.JwtUtil;

@Service
public class AdminService {

    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    public AdminService(AdminRepository adminRepository, PasswordEncoder passwordEncoder,
                        JwtUtil jwtUtil, AuthenticationManager authenticationManager) {
        this.adminRepository = adminRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
    }

    // Register Admin
    public ResponseEntity<Map<String, Object>> registerAdmin(AdminDTO dto) {
        if (adminRepository.findByEmail(dto.getEmail()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("message", "Email already exists!"));
        }

        Admin admin = new Admin();
        admin.setName(dto.getName());
        admin.setEmail(dto.getEmail());
        admin.setPasswordHash(passwordEncoder.encode(dto.getPassword()));
        admin.setPhone(dto.getPhone());
        admin.setRole(Role.ADMIN);

        adminRepository.save(admin);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of("message", "Admin Registration Successful!", "role", admin.getRole()));
    }

    // Login Admin
    public ResponseEntity<ApiResponse<JwtLoginResponse>> loginAdmin(LoginRequest loginRequest) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

            Admin admin = adminRepository.findByEmail(loginRequest.getEmail())
                    .orElseThrow(() -> new RuntimeException("Admin not found!"));

            String token = jwtUtil.createToken(admin.getEmail());
            JwtLoginResponse jwtResponse = new JwtLoginResponse(token, admin.getEmail(), admin.getRole());

            return ResponseEntity.ok(new ApiResponse<>(true, "Login successful!", jwtResponse));

        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiResponse<>(false, "Invalid Credentials!", null));
        }
    }
}