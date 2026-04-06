package com.college.student.portal.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.college.student.portal.dto.*;
import com.college.student.portal.service.FacultyService;

import jakarta.validation.Valid;

@RestController
public class FacultyController {

    private final FacultyService facultyService;

    public FacultyController(FacultyService facultyService) {
        this.facultyService = facultyService;
    }

    @PostMapping("/api/auth/faculty/register")
    public ResponseEntity<Map<String, Object>> register(@Valid @RequestBody FacultyDTO dto) {
        return facultyService.registerFaculty(dto);
    }

    @PostMapping("/api/auth/faculty/login")
    public ResponseEntity<ApiResponse<JwtLoginResponse>> login(@RequestBody LoginRequest loginRequest) {
        return facultyService.loginFaculty(loginRequest);
    }
}