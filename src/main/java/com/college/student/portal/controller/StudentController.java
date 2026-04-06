package com.college.student.portal.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.college.student.portal.dto.*;
import com.college.student.portal.service.StudentService;

import jakarta.validation.Valid;

@RestController
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    // Register
    @PostMapping("/api/auth/student/register")
    public ResponseEntity<Map<String, Object>> register(@Valid @RequestBody StudentDTO dto) {
        return studentService.registerStudent(dto);
    }

    // Login
    @PostMapping("/api/auth/student/login")
    public ResponseEntity<ApiResponse<JwtLoginResponse>> login(@RequestBody LoginRequest loginRequest) {
        return studentService.loginStudent(loginRequest);
    }

    // Get all students (Admin/Faculty)
    @GetMapping("/api/admin/students")
    public List<StudentResponseDTO> getAllStudents() {
        return studentService.getAllStudents();
    }

    // Get student by ID
    @GetMapping("/api/students/{id}")
    public StudentResponseDTO getStudent(@PathVariable int id) {
        return studentService.getStudentById(id);
    }

    // Update student profile
    @PatchMapping("/api/students/{id}")
    public ResponseEntity<Map<String, Object>> updateStudent(
            @Valid @RequestBody StudentUpdateDTO dto, @PathVariable int id) {
        return studentService.updateStudent(dto, id);
    }

    // Get profile (self)
    @GetMapping("/api/students/me")
    public StudentResponseDTO getMyProfile(Authentication authentication) {
        String email = authentication.getName();
        // Find by email then return
        return studentService.getAllStudents().stream()
                .filter(s -> s.getEmail().equals(email)).findFirst()
                .orElseThrow(() -> new RuntimeException("Student not found!"));
    }

    // Change password
    @PutMapping("/api/students/change-password")
    public ResponseEntity<Map<String, Object>> changePassword(
            Authentication authentication, @Valid @RequestBody ChangePasswordDTO dto) {
        return studentService.changePassword(authentication.getName(), dto);
    }

    // Filter by branch & semester
    @GetMapping("/api/admin/students/filter")
    public List<StudentResponseDTO> filterStudents(
            @RequestParam String branch, @RequestParam String semester) {
        return studentService.getStudentsByBranchAndSemester(branch, semester);
    }
}