package com.college.student.portal.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.college.student.portal.entity.Marks;

@Repository
public interface MarksRepository extends JpaRepository<Marks, Integer> {

    Optional<Marks> findByStudent_IdAndCourse_Id(Integer studentId, Integer courseId);
}