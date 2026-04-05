package com.college.student.portal.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EnrollmentDTO {

    @NotNull(message = "Course ID is required")
    private Integer courseId;

    @NotNull(message = "Subject ID is required")
    private Integer subjectId;
}