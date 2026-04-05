package com.college.student.portal.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourseResponseDTO {

    private Integer id;
    private String subjectCode;
    private String subjectName;
    private String facultyName;
    private String semester;
    private String section;
    private String academicYear;
    private int totalClasses;
}