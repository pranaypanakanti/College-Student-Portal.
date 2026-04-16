package com.college.student.portal.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.college.student.portal.dto.AdminDTO;
import com.college.student.portal.dto.ApiResponse;
import com.college.student.portal.dto.JwtLoginResponse;
import com.college.student.portal.dto.LoginRequest;
import com.college.student.portal.entity.Admin;
import com.college.student.portal.enums.Role;
import com.college.student.portal.repository.AdminRepository;
import com.college.student.portal.security.JwtUtil;

@ExtendWith(MockitoExtension.class)
class AdminServiceTest {

    @Mock private AdminRepository adminRepository;
    @Mock private PasswordEncoder passwordEncoder;
    @Mock private JwtUtil jwtUtil;
    @Mock private AuthenticationManager authenticationManager;

    @InjectMocks private AdminService adminService;

    @Test
    void testRegisterAdmin_Success() {
        AdminDTO dto = new AdminDTO("Admin", "admin@test.com", "password123", "9999999999");

        when(adminRepository.findByEmail(any())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(any())).thenReturn("encoded");
        when(adminRepository.save(any())).thenReturn(new Admin());

        ResponseEntity<?> response = adminService.registerAdmin(dto);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        verify(adminRepository).save(any(Admin.class));
    }

    @Test
    void testRegisterAdmin_DuplicateEmail() {
        AdminDTO dto = new AdminDTO("Admin", "admin@test.com", "password123", "9999999999");

        when(adminRepository.findByEmail(any())).thenReturn(Optional.of(new Admin()));

        ResponseEntity<?> response = adminService.registerAdmin(dto);
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        verify(adminRepository, never()).save(any());
    }

    @Test
    void testLoginAdmin_Success() {
        LoginRequest loginRequest = new LoginRequest("admin@test.com", "password123");

        Admin admin = new Admin();
        admin.setEmail("admin@test.com");
        admin.setRole(Role.ADMIN);

        when(authenticationManager.authenticate(any())).thenReturn(
                new UsernamePasswordAuthenticationToken("admin@test.com", "password123"));
        when(adminRepository.findByEmail("admin@test.com")).thenReturn(Optional.of(admin));
        when(jwtUtil.createToken("admin@test.com")).thenReturn("mock-jwt-token");

        ResponseEntity<ApiResponse<JwtLoginResponse>> response = adminService.loginAdmin(loginRequest);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().isSuccess());
    }

    @Test
    void testLoginAdmin_BadCredentials() {
        LoginRequest loginRequest = new LoginRequest("admin@test.com", "wrong");

        when(authenticationManager.authenticate(any())).thenThrow(new BadCredentialsException("Bad"));

        ResponseEntity<ApiResponse<JwtLoginResponse>> response = adminService.loginAdmin(loginRequest);
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertFalse(response.getBody().isSuccess());
    }
}