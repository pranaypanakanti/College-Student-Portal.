package com.college.student.portal.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.college.student.portal.dto.FeePaymentDTO;
import com.college.student.portal.dto.FeePaymentResponseDTO;
import com.college.student.portal.entity.*;
import com.college.student.portal.repository.*;
import com.college.student.portal.util.GradeUtil;

@Service
public class FeePaymentService {

    private final FeePaymentRepository feePaymentRepository;
    private final StudentRepository studentRepository;
    private final FeeStructureRepository feeStructureRepository;

    public FeePaymentService(FeePaymentRepository feePaymentRepository, StudentRepository studentRepository,
                             FeeStructureRepository feeStructureRepository) {
        this.feePaymentRepository = feePaymentRepository;
        this.studentRepository = studentRepository;
        this.feeStructureRepository = feeStructureRepository;
    }

    public ResponseEntity<Map<String, Object>> makePayment(int studentId, FeePaymentDTO dto) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found!"));
        FeeStructure feeStructure = feeStructureRepository.findById(dto.getFeeStructureId())
                .orElseThrow(() -> new RuntimeException("Fee structure not found!"));

        FeePayment payment = new FeePayment();
        payment.setStudent(student);
        payment.setFeeStructure(feeStructure);
        payment.setAmountPaid(dto.getAmountPaid());
        payment.setPaymentDate(LocalDate.now());
        payment.setTransactionId(dto.getTransactionId());
        payment.setPaymentStatus(dto.getAmountPaid() >= feeStructure.getTotalFee() ? "PAID" : "PARTIAL");
        payment.setReceiptNumber(GradeUtil.generateReceiptNumber());
        feePaymentRepository.save(payment);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of("message", "Payment recorded!", "receipt", payment.getReceiptNumber(),
                        "status", payment.getPaymentStatus()));
    }

    public List<FeePaymentResponseDTO> getStudentPayments(int studentId) {
        return feePaymentRepository.findByStudent_Id(studentId).stream()
                .map(p -> new FeePaymentResponseDTO(
                        p.getReceiptNumber(), p.getAmountPaid(),
                        p.getFeeStructure().getTotalFee(),
                        p.getFeeStructure().getTotalFee() - p.getAmountPaid(),
                        p.getPaymentStatus(), p.getPaymentDate(), p.getTransactionId()))
                .toList();
    }
}