package com.college.student.portal.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.college.student.portal.entity.Course;

@Repository
public interface CourseRepository extends JpaRepository<Course, Integer> {

    Optional<Course> findBySubject_SubjectCodeAndSectionAndAcademicYear(
            String subjectCode, String section, String academicYear);
}