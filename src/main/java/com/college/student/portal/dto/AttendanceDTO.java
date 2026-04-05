package com.college.student.portal.dto;

import java.time.LocalDate;

import com.college.student.portal.enums.AttendanceStatus;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AttendanceDTO {

    @NotNull(message = "Student ID is required")
    private Integer studentId;

    @NotNull(message = "Date is required")
    @PastOrPresent(message = "Date cannot be in the future")
    private LocalDate classDate;

    @NotNull(message = "Status is required")
    private AttendanceStatus status;
}