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
import com.college.student.portal.entity.Faculty;
import com.college.student.portal.enums.Role;
import com.college.student.portal.repository.FacultyRepository;
import com.college.student.portal.security.JwtUtil;

@Service
public class FacultyService {

    private final FacultyRepository facultyRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    public FacultyService(FacultyRepository facultyRepository, PasswordEncoder passwordEncoder,
                          JwtUtil jwtUtil, AuthenticationManager authenticationManager) {
        this.facultyRepository = facultyRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
    }

    // Register Faculty
    public ResponseEntity<Map<String, Object>> registerFaculty(FacultyDTO dto) {
        if (facultyRepository.findByEmail(dto.getEmail()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("message", "Email already exists!"));
        }

        Faculty faculty = new Faculty();
        faculty.setName(dto.getName());
        faculty.setEmail(dto.getEmail());
        faculty.setPhone(dto.getPhone());
        faculty.setPasswordHash(passwordEncoder.encode(dto.getPassword()));
        faculty.setDesignation(dto.getDesignation());
        faculty.setDepartment(dto.getDepartment());
        faculty.setQualification(dto.getQualification());
        faculty.setExperienceYears(dto.getExperienceYears());
        faculty.setRole(Role.FACULTY);

        facultyRepository.save(faculty);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of("message", "Faculty Registration Successful!", "role", faculty.getRole()));
    }

    // Login Faculty
    public ResponseEntity<ApiResponse<JwtLoginResponse>> loginFaculty(LoginRequest loginRequest) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

            Faculty faculty = facultyRepository.findByEmail(loginRequest.getEmail())
                    .orElseThrow(() -> new RuntimeException("Faculty not found!"));

            String token = jwtUtil.createToken(faculty.getEmail());
            JwtLoginResponse jwtResponse = new JwtLoginResponse(token, faculty.getEmail(), faculty.getRole());

            return ResponseEntity.ok(new ApiResponse<>(true, "Login successful!", jwtResponse));

        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiResponse<>(false, "Invalid Credentials!", null));
        }
    }
}