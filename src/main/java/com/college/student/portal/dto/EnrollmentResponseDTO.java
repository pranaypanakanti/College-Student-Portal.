package com.college.student.portal.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EnrollmentResponseDTO {

    private Integer enrollmentId;
    private String subjectCode;
    private String subjectName;
    private String semester;
    private String academicYear;
    private String status;
    private String enrolledDate;
}