package com.college.student.portal.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MarksDTO {

    @NotNull(message = "Student ID is required")
    private Integer studentId;

    @NotNull(message = "Subject ID is required")
    private Integer subjectId;

    @Min(0) @Max(100)
    private int theoryMarks;

    @Min(0) @Max(100)
    private int practicalMarks;

    private String semester;
    private String academicYear;
}