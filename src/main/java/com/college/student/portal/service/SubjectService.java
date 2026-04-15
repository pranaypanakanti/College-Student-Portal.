package com.college.student.portal.service;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.college.student.portal.dto.SubjectDTO;
import com.college.student.portal.entity.Subject;
import com.college.student.portal.repository.SubjectRepository;

@Service
public class SubjectService {

    private final SubjectRepository subjectRepository;

    public SubjectService(SubjectRepository subjectRepository) {
        this.subjectRepository = subjectRepository;
    }

    public ResponseEntity<Map<String, Object>> addSubject(SubjectDTO dto) {
        if (subjectRepository.findBySubjectCode(dto.getSubjectCode()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("message", "Subject already exists with code: " + dto.getSubjectCode()));
        }
        Subject subject = new Subject();
        subject.setSubjectCode(dto.getSubjectCode());
        subject.setSubjectName(dto.getSubjectName());
        subject.setBranch(dto.getBranch());
        subject.setSemester(dto.getSemester());
        subject.setCredits(dto.getCredits());
        subject.setTheoryMarks(dto.getTheoryMarks());
        subject.setPracticalMarks(dto.getPracticalMarks());
        subjectRepository.save(subject);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of("message", "Subject added successfully!"));
    }

    public List<Subject> getAllSubjects() {
        return subjectRepository.findAll();
    }

    public List<Subject> filterSubjects(String branch, String semester) {
        return subjectRepository.findByBranchAndSemester(branch, semester);
    }
}