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

import com.college.student.portal.dto.ApiResponse;
import com.college.student.portal.dto.FacultyDTO;
import com.college.student.portal.dto.JwtLoginResponse;
import com.college.student.portal.dto.LoginRequest;
import com.college.student.portal.entity.Faculty;
import com.college.student.portal.enums.Role;
import com.college.student.portal.repository.FacultyRepository;
import com.college.student.portal.security.JwtUtil;

@ExtendWith(MockitoExtension.class)
class FacultyServiceTest {

    @Mock private FacultyRepository facultyRepository;
    @Mock private PasswordEncoder passwordEncoder;
    @Mock private JwtUtil jwtUtil;
    @Mock private AuthenticationManager authenticationManager;

    @InjectMocks private FacultyService facultyService;

    @Test
    void testRegisterFaculty_Success() {
        FacultyDTO dto = new FacultyDTO();
        dto.setName("Dr. Test");
        dto.setEmail("faculty@test.com");
        dto.setPhone("9876543210");
        dto.setPassword("password123");
        dto.setDesignation("Professor");
        dto.setDepartment("CSE");

        when(facultyRepository.findByEmail(any())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(any())).thenReturn("encoded");
        when(facultyRepository.save(any())).thenReturn(new Faculty());

        ResponseEntity<?> response = facultyService.registerFaculty(dto);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        verify(facultyRepository).save(any(Faculty.class));
    }

    @Test
    void testRegisterFaculty_DuplicateEmail() {
        FacultyDTO dto = new FacultyDTO();
        dto.setEmail("existing@test.com");

        when(facultyRepository.findByEmail(any())).thenReturn(Optional.of(new Faculty()));

        ResponseEntity<?> response = facultyService.registerFaculty(dto);
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        verify(facultyRepository, never()).save(any());
    }

    @Test
    void testLoginFaculty_Success() {
        LoginRequest loginRequest = new LoginRequest("faculty@test.com", "password123");

        Faculty faculty = new Faculty();
        faculty.setEmail("faculty@test.com");
        faculty.setRole(Role.FACULTY);

        when(authenticationManager.authenticate(any())).thenReturn(
                new UsernamePasswordAuthenticationToken("faculty@test.com", "password123"));
        when(facultyRepository.findByEmail("faculty@test.com")).thenReturn(Optional.of(faculty));
        when(jwtUtil.createToken("faculty@test.com")).thenReturn("mock-jwt-token");

        ResponseEntity<ApiResponse<JwtLoginResponse>> response = facultyService.loginFaculty(loginRequest);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().isSuccess());
        assertEquals("mock-jwt-token", response.getBody().getData().getToken());
    }

    @Test
    void testLoginFaculty_BadCredentials() {
        LoginRequest loginRequest = new LoginRequest("faculty@test.com", "wrongpassword");

        when(authenticationManager.authenticate(any())).thenThrow(new BadCredentialsException("Bad"));

        ResponseEntity<ApiResponse<JwtLoginResponse>> response = facultyService.loginFaculty(loginRequest);
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertFalse(response.getBody().isSuccess());
    }
}