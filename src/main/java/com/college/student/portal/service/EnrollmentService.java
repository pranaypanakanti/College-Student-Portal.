package com.college.student.portal.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.college.student.portal.dto.EnrollmentDTO;
import com.college.student.portal.dto.EnrollmentResponseDTO;
import com.college.student.portal.entity.*;
import com.college.student.portal.enums.EnrollmentStatus;
import com.college.student.portal.repository.*;

@Service
public class EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;
    private final SubjectRepository subjectRepository;

    public EnrollmentService(EnrollmentRepository enrollmentRepository, StudentRepository studentRepository,
                             CourseRepository courseRepository, SubjectRepository subjectRepository) {
        this.enrollmentRepository = enrollmentRepository;
        this.studentRepository = studentRepository;
        this.courseRepository = courseRepository;
        this.subjectRepository = subjectRepository;
    }

    public ResponseEntity<Map<String, Object>> enrollStudent(int studentId, EnrollmentDTO dto) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found!"));
        Course course = courseRepository.findById(dto.getCourseId())
                .orElseThrow(() -> new RuntimeException("Course not found!"));
        Subject subject = subjectRepository.findById(dto.getSubjectId())
                .orElseThrow(() -> new RuntimeException("Subject not found!"));

        if (enrollmentRepository.findByStudent_IdAndCourse_Id(studentId, dto.getCourseId()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("message", "Student already enrolled in this course!"));
        }

        Enrollment enrollment = new Enrollment();
        enrollment.setStudent(student);
        enrollment.setCourse(course);
        enrollment.setSubject(subject);
        enrollment.setSemester(course.getSemester());
        enrollment.setAcademicYear(course.getAcademicYear());
        enrollment.setStatus(EnrollmentStatus.ACTIVE);
        enrollment.setEnrolledDate(LocalDate.now());
        enrollmentRepository.save(enrollment);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of("message", "Enrolled successfully!", "enrollmentId", enrollment.getId()));
    }

    public List<EnrollmentResponseDTO> getStudentEnrollments(int studentId) {
        return enrollmentRepository.findByStudent_IdAndStatus(studentId, EnrollmentStatus.ACTIVE).stream()
                .map(e -> new EnrollmentResponseDTO(
                        e.getId(),
                        e.getSubject().getSubjectCode(),
                        e.getSubject().getSubjectName(),
                        e.getSemester(),
                        e.getAcademicYear(),
                        e.getStatus().name(),
                        e.getEnrolledDate().toString()))
                .toList();
    }
}