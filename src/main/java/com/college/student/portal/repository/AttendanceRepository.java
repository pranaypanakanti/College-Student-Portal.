package com.college.student.portal.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.college.student.portal.entity.Attendance;
import com.college.student.portal.entity.Course;
import com.college.student.portal.entity.Student;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Integer> {

    Optional<Attendance> findByStudentAndClassDateAndCourse(Student student, LocalDate classDate, Course course);

    List<Attendance> findByCourseAndClassDateBetween(Course course, LocalDate start, LocalDate end);

    List<Attendance> findByStudentAndCourse(Student student, Course course);
}