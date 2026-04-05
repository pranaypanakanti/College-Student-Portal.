package com.college.student.portal.dto;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FeePaymentResponseDTO {

    private String receiptNumber;
    private Long amountPaid;
    private Long totalFee;
    private Long balanceDue;
    private String paymentStatus;
    private LocalDate paymentDate;
    private String transactionId;
}