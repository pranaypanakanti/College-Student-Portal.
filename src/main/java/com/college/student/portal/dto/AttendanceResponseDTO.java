package com.college.student.portal.dto;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AttendanceResponseDTO {

    private String studentRollNumber;
    private String studentName;
    private LocalDate classDate;
    private String status;
}