package com.college.student.portal.dto;

import java.time.LocalDate;

import com.college.student.portal.enums.TargetAudience;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnnouncementDTO {

    @NotBlank(message = "Title is required")
    @Size(max = 200)
    private String title;

    @NotBlank(message = "Description is required")
    private String description;

    @NotNull(message = "Target audience is required")
    private TargetAudience targetAudience;

    private LocalDate expiresAt;
}