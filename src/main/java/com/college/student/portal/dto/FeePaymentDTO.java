package com.college.student.portal.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FeePaymentDTO {

    @NotNull(message = "Fee structure ID is required")
    private Integer feeStructureId;

    @NotNull(message = "Amount is required")
    @Min(1)
    private Long amountPaid;

    private String transactionId;
}