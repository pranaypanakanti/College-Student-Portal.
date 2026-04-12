package com.college.student.portal.service;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.college.student.portal.dto.CourseDTO;
import com.college.student.portal.dto.CourseResponseDTO;
import com.college.student.portal.entity.Course;
import com.college.student.portal.entity.Faculty;
import com.college.student.portal.entity.Subject;
import com.college.student.portal.repository.CourseRepository;
import com.college.student.portal.repository.FacultyRepository;
import com.college.student.portal.repository.SubjectRepository;

@Service
public class CourseService {

    private final CourseRepository courseRepository;
    private final FacultyRepository facultyRepository;
    private final SubjectRepository subjectRepository;

    public CourseService(CourseRepository courseRepository, FacultyRepository facultyRepository,
                         SubjectRepository subjectRepository) {
        this.courseRepository = courseRepository;
        this.facultyRepository = facultyRepository;
        this.subjectRepository = subjectRepository;
    }

    public ResponseEntity<Map<String, Object>> createCourse(CourseDTO dto) {
        Faculty faculty = facultyRepository.findById(dto.getFacultyId())
                .orElseThrow(() -> new RuntimeException("Faculty not found!"));
        Subject subject = subjectRepository.findById(dto.getSubjectId())
                .orElseThrow(() -> new RuntimeException("Subject not found!"));

        if (courseRepository.findBySubject_SubjectCodeAndSectionAndAcademicYear(
                subject.getSubjectCode(), dto.getSection(), dto.getAcademicYear()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("message", "Course already exists for this subject/section/year!"));
        }

        Course course = new Course();
        course.setFaculty(faculty);
        course.setSubject(subject);
        course.setSemester(dto.getSemester());
        course.setSection(dto.getSection());
        course.setAcademicYear(dto.getAcademicYear());
        course.setTotalClasses(dto.getTotalClasses());
        courseRepository.save(course);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of("message", "Course created successfully!", "courseId", course.getId()));
    }

    public List<CourseResponseDTO> getAllCourses() {
        return courseRepository.findAll().stream()
                .map(c -> new CourseResponseDTO(
                        c.getId(),
                        c.getSubject().getSubjectCode(),
                        c.getSubject().getSubjectName(),
                        c.getFaculty().getName(),
                        c.getSemester(),
                        c.getSection(),
                        c.getAcademicYear(),
                        c.getTotalClasses()))
                .toList();
    }

    public CourseResponseDTO getCourseById(int id) {
        Course c = courseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Course not found!"));
        return new CourseResponseDTO(c.getId(), c.getSubject().getSubjectCode(),
                c.getSubject().getSubjectName(), c.getFaculty().getName(),
                c.getSemester(), c.getSection(), c.getAcademicYear(), c.getTotalClasses());
    }
}