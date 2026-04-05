package com.college.student.portal.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FeeStructureDTO {

    @NotBlank(message = "Branch is required")
    private String branch;

    @NotBlank(message = "Semester is required")
    private String semester;

    @NotNull @Min(0)
    private Long tuitionFee;

    @NotNull @Min(0)
    private Long hostelFee;

    @NotNull @Min(0)
    private Long libraryFee;

    @NotNull @Min(0)
    private Long labFee;

    private LocalDate dueDate;
}