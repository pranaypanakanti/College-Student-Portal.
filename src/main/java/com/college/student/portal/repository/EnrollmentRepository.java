package com.college.student.portal.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.college.student.portal.entity.Enrollment;
import com.college.student.portal.enums.EnrollmentStatus;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, Integer> {

    Optional<Enrollment> findByStudent_IdAndCourse_Id(Integer studentId, Integer courseId);

    List<Enrollment> findByStudent_IdAndStatus(Integer studentId, EnrollmentStatus status);

    List<Enrollment> findByCourse_Id(Integer courseId);
}