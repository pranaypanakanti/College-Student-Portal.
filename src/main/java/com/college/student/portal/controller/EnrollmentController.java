package com.college.student.portal.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.college.student.portal.dto.EnrollmentDTO;
import com.college.student.portal.dto.EnrollmentResponseDTO;
import com.college.student.portal.service.EnrollmentService;

import jakarta.validation.Valid;

@RestController
public class EnrollmentController {

    private final EnrollmentService enrollmentService;

    public EnrollmentController(EnrollmentService enrollmentService) {
        this.enrollmentService = enrollmentService;
    }

    @PostMapping("/api/students/{studentId}/enroll")
    public ResponseEntity<Map<String, Object>> enroll(
            @PathVariable int studentId, @Valid @RequestBody EnrollmentDTO dto) {
        return enrollmentService.enrollStudent(studentId, dto);
    }

    @GetMapping("/api/students/{studentId}/courses")
    public List<EnrollmentResponseDTO> getEnrolledCourses(@PathVariable int studentId) {
        return enrollmentService.getStudentEnrollments(studentId);
    }
}