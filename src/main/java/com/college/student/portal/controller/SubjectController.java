package com.college.student.portal.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.college.student.portal.dto.SubjectDTO;
import com.college.student.portal.entity.Subject;
import com.college.student.portal.service.SubjectService;

import jakarta.validation.Valid;

@RestController
public class SubjectController {

    private final SubjectService subjectService;

    public SubjectController(SubjectService subjectService) {
        this.subjectService = subjectService;
    }

    @PostMapping("/api/admin/subjects")
    public ResponseEntity<Map<String, Object>> addSubject(@Valid @RequestBody SubjectDTO dto) {
        return subjectService.addSubject(dto);
    }

    @GetMapping("/api/admin/subjects")
    public List<Subject> getAllSubjects() {
        return subjectService.getAllSubjects();
    }

    @GetMapping("/api/admin/subjects/filter")
    public List<Subject> filterSubjects(@RequestParam String branch, @RequestParam String semester) {
        return subjectService.filterSubjects(branch, semester);
    }
}