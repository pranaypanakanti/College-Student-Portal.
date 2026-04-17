package com.college.student.portal.service;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.college.student.portal.dto.MarksDTO;
import com.college.student.portal.dto.MarksResponseDTO;
import com.college.student.portal.entity.*;
import com.college.student.portal.repository.*;
import com.college.student.portal.util.GradeUtil;

@Service
public class MarksService {

    private final MarksRepository marksRepository;
    private final StudentRepository studentRepository;
    private final SubjectRepository subjectRepository;
    private final CourseRepository courseRepository;

    public MarksService(MarksRepository marksRepository, StudentRepository studentRepository,
                        SubjectRepository subjectRepository, CourseRepository courseRepository) {
        this.marksRepository = marksRepository;
        this.studentRepository = studentRepository;
        this.subjectRepository = subjectRepository;
        this.courseRepository = courseRepository;
    }

    public ResponseEntity<Map<String, Object>> enterMarks(MarksDTO dto, int courseId) {
        Student student = studentRepository.findById(dto.getStudentId())
                .orElseThrow(() -> new RuntimeException("Student not found!"));
        Subject subject = subjectRepository.findById(dto.getSubjectId())
                .orElseThrow(() -> new RuntimeException("Subject not found!"));
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found!"));

        if (marksRepository.findByStudent_IdAndCourse_Id(dto.getStudentId(), courseId).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("message", "Marks already entered for this student in this course!"));
        }

        int total = dto.getTheoryMarks() + dto.getPracticalMarks();
        String grade = GradeUtil.calculateGrade(total);

        Marks marks = new Marks();
        marks.setStudent(student);
        marks.setSubject(subject);
        marks.setCourse(course);
        marks.setTheoryMarks(dto.getTheoryMarks());
        marks.setPracticalMarks(dto.getPracticalMarks());
        marks.setTotalMarks(total);
        marks.setGrade(grade);
        marks.setSemester(dto.getSemester());
        marks.setAcademicYear(dto.getAcademicYear());
        marksRepository.save(marks);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of("message", "Marks entered successfully!", "grade", grade, "total", total));
    }

    public List<MarksResponseDTO> getStudentMarks(int studentId) {
        return marksRepository.findAll().stream()
                .filter(m -> m.getStudent().getId().equals(studentId))
                .map(m -> new MarksResponseDTO(
                        m.getStudent().getRollNumber(), m.getStudent().getName(),
                        m.getSubject().getSubjectCode(), m.getSubject().getSubjectName(),
                        m.getTheoryMarks(), m.getPracticalMarks(), m.getTotalMarks(),
                        m.getGrade(), m.getSemester(), m.getAcademicYear()))
                .toList();
    }
}