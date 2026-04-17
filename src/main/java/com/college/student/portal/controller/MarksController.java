package com.college.student.portal.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.college.student.portal.dto.MarksDTO;
import com.college.student.portal.dto.MarksResponseDTO;
import com.college.student.portal.service.MarksService;

import jakarta.validation.Valid;

@RestController
public class MarksController {

    private final MarksService marksService;

    public MarksController(MarksService marksService) {
        this.marksService = marksService;
    }

    @PostMapping("/api/faculty/marks/{courseId}")
    public ResponseEntity<Map<String, Object>> enterMarks(
            @PathVariable int courseId, @Valid @RequestBody MarksDTO dto) {
        return marksService.enterMarks(dto, courseId);
    }

    @GetMapping("/api/students/{studentId}/marks")
    public List<MarksResponseDTO> getStudentMarks(@PathVariable int studentId) {
        return marksService.getStudentMarks(studentId);
    }
}