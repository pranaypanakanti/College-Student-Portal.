package com.college.student.portal.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.college.student.portal.dto.AttendanceDTO;
import com.college.student.portal.dto.AttendanceResponseDTO;
import com.college.student.portal.service.AttendanceService;

import jakarta.validation.Valid;

@RestController
public class AttendanceController {

    private final AttendanceService attendanceService;

    public AttendanceController(AttendanceService attendanceService) {
        this.attendanceService = attendanceService;
    }

    @PostMapping("/api/faculty/{facultyId}/attendance/{courseId}")
    public ResponseEntity<Map<String, Object>> markAttendance(
            @PathVariable int facultyId, @PathVariable int courseId,
            @Valid @RequestBody AttendanceDTO dto) {
        return attendanceService.markAttendance(dto, courseId, facultyId);
    }

    @GetMapping("/api/courses/{courseId}/attendance")
    public List<AttendanceResponseDTO> getCourseAttendance(@PathVariable int courseId) {
        return attendanceService.getAttendanceByCourse(courseId);
    }

    @GetMapping("/api/students/{studentId}/attendance/{courseId}")
    public List<AttendanceResponseDTO> getStudentAttendance(
            @PathVariable int studentId, @PathVariable int courseId) {
        return attendanceService.getStudentAttendance(studentId, courseId);
    }
}