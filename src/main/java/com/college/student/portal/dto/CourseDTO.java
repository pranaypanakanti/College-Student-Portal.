package com.college.student.portal.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourseDTO {

    private Integer id;

    @NotNull(message = "Faculty ID is required")
    private Integer facultyId;

    @NotNull(message = "Subject ID is required")
    private Integer subjectId;

    @NotBlank(message = "Semester is required")
    private String semester;

    @NotBlank(message = "Section is required")
    private String section;

    @NotBlank(message = "Academic year is required")
    private String academicYear;

    @Min(value = 1, message = "Total classes must be at least 1")
    private int totalClasses;
}