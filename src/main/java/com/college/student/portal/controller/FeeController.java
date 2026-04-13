package com.college.student.portal.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.college.student.portal.dto.FeePaymentDTO;
import com.college.student.portal.dto.FeePaymentResponseDTO;
import com.college.student.portal.dto.FeeStructureDTO;
import com.college.student.portal.entity.FeeStructure;
import com.college.student.portal.service.FeePaymentService;
import com.college.student.portal.service.FeeStructureService;

import jakarta.validation.Valid;

@RestController
public class FeeController {

    private final FeeStructureService feeStructureService;
    private final FeePaymentService feePaymentService;

    public FeeController(FeeStructureService feeStructureService, FeePaymentService feePaymentService) {
        this.feeStructureService = feeStructureService;
        this.feePaymentService = feePaymentService;
    }

    @PostMapping("/api/admin/fee-structure")
    public ResponseEntity<Map<String, Object>> createFeeStructure(@Valid @RequestBody FeeStructureDTO dto) {
        return feeStructureService.createFeeStructure(dto);
    }

    @GetMapping("/api/admin/fee-structure")
    public List<FeeStructure> getAllFeeStructures() {
        return feeStructureService.getAllFeeStructures();
    }

    @PostMapping("/api/students/{studentId}/pay-fee")
    public ResponseEntity<Map<String, Object>> payFee(
            @PathVariable int studentId, @Valid @RequestBody FeePaymentDTO dto) {
        return feePaymentService.makePayment(studentId, dto);
    }

    @GetMapping("/api/students/{studentId}/fees")
    public List<FeePaymentResponseDTO> getStudentFees(@PathVariable int studentId) {
        return feePaymentService.getStudentPayments(studentId);
    }
}