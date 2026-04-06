package com.college.student.portal.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.college.student.portal.dto.*;
import com.college.student.portal.service.AdminService;

import jakarta.validation.Valid;

@RestController
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @PostMapping("/api/auth/admin/register")
    public ResponseEntity<Map<String, Object>> register(@Valid @RequestBody AdminDTO dto) {
        return adminService.registerAdmin(dto);
    }

    @PostMapping("/api/auth/admin/login")
    public ResponseEntity<ApiResponse<JwtLoginResponse>> login(@RequestBody LoginRequest loginRequest) {
        return adminService.loginAdmin(loginRequest);
    }
}