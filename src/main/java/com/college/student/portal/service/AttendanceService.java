package com.college.student.portal.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.college.student.portal.dto.AttendanceDTO;
import com.college.student.portal.dto.AttendanceResponseDTO;
import com.college.student.portal.entity.*;
import com.college.student.portal.repository.*;

@Service
public class AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;
    private final FacultyRepository facultyRepository;

    public AttendanceService(AttendanceRepository attendanceRepository, StudentRepository studentRepository,
                             CourseRepository courseRepository, FacultyRepository facultyRepository) {
        this.attendanceRepository = attendanceRepository;
        this.studentRepository = studentRepository;
        this.courseRepository = courseRepository;
        this.facultyRepository = facultyRepository;
    }

    public ResponseEntity<Map<String, Object>> markAttendance(AttendanceDTO dto, int courseId, int facultyId) {
        Student student = studentRepository.findById(dto.getStudentId())
                .orElseThrow(() -> new RuntimeException("Student not found!"));
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found!"));
        Faculty faculty = facultyRepository.findById(facultyId)
                .orElseThrow(() -> new RuntimeException("Faculty not found!"));

        Optional<Attendance> existing = attendanceRepository.findByStudentAndClassDateAndCourse(
                student, dto.getClassDate(), course);
        if (existing.isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("message", "Attendance already marked for this student on this date!"));
        }

        Attendance attendance = new Attendance();
        attendance.setStudent(student);
        attendance.setCourse(course);
        attendance.setClassDate(dto.getClassDate());
        attendance.setStatus(dto.getStatus());
        attendance.setMarkedBy(faculty);
        attendanceRepository.save(attendance);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of("message", "Attendance marked successfully!"));
    }

    public List<AttendanceResponseDTO> getAttendanceByCourse(int courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found!"));
        return attendanceRepository.findByStudentAndCourse(null, course).isEmpty()
                ? attendanceRepository.findAll().stream()
                .filter(a -> a.getCourse().getId().equals(courseId))
                .map(a -> new AttendanceResponseDTO(
                        a.getStudent().getRollNumber(),
                        a.getStudent().getName(),
                        a.getClassDate(),
                        a.getStatus().name()))
                .toList()
                : List.of();
    }

    public List<AttendanceResponseDTO> getStudentAttendance(int studentId, int courseId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found!"));
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found!"));
        return attendanceRepository.findByStudentAndCourse(student, course).stream()
                .map(a -> new AttendanceResponseDTO(
                        a.getStudent().getRollNumber(),
                        a.getStudent().getName(),
                        a.getClassDate(),
                        a.getStatus().name()))
                .toList();
    }
}