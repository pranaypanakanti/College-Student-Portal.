package com.college.student.portal.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.college.student.portal.dto.CourseDTO;
import com.college.student.portal.dto.CourseResponseDTO;
import com.college.student.portal.service.CourseService;

import jakarta.validation.Valid;

@RestController
public class CourseController {

    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @PostMapping("/api/admin/courses")
    public ResponseEntity<Map<String, Object>> createCourse(@Valid @RequestBody CourseDTO dto) {
        return courseService.createCourse(dto);
    }

    @GetMapping("/api/admin/courses")
    public List<CourseResponseDTO> getAllCourses() {
        return courseService.getAllCourses();
    }

    @GetMapping("/api/courses/{id}")
    public CourseResponseDTO getCourse(@PathVariable int id) {
        return courseService.getCourseById(id);
    }
}