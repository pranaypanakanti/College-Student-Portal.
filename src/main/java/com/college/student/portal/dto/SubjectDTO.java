package com.college.student.portal.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubjectDTO {

    @NotBlank(message = "Subject code is required")
    @Size(min = 3, max = 10)
    private String subjectCode;

    @NotBlank(message = "Subject name is required")
    @Size(min = 3, max = 100)
    private String subjectName;

    @NotBlank(message = "Branch is required")
    private String branch;

    @NotBlank(message = "Semester is required")
    private String semester;

    @NotNull(message = "Credits required")
    @Positive
    private Integer credits;

    @Min(0) private int theoryMarks;
    @Min(0) private int practicalMarks;
}