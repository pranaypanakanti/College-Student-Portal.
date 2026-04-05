package com.college.student.portal.dto;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnnouncementResponseDTO {

    private Integer id;
    private String title;
    private String description;
    private String targetAudience;
    private String createdByName;
    private LocalDate createdAt;
    private LocalDate expiresAt;
}