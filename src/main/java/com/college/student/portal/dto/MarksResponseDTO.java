package com.college.student.portal.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MarksResponseDTO {

    private String studentRollNumber;
    private String studentName;
    private String subjectCode;
    private String subjectName;
    private int theoryMarks;
    private int practicalMarks;
    private int totalMarks;
    private String grade;
    private String semester;
    private String academicYear;
}