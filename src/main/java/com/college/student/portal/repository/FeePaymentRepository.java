package com.college.student.portal.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.college.student.portal.entity.FeePayment;

@Repository
public interface FeePaymentRepository extends JpaRepository<FeePayment, Integer> {

    List<FeePayment> findByStudent_Id(Integer studentId);
}