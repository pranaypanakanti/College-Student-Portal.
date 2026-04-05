package com.college.student.portal.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.college.student.portal.entity.FeeStructure;

@Repository
public interface FeeStructureRepository extends JpaRepository<FeeStructure, Integer> {

    Optional<FeeStructure> findByBranchAndSemester(String branch, String semester);
}