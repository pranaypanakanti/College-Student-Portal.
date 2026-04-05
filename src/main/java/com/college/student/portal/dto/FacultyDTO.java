package com.college.student.portal.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FacultyDTO {

    @NotBlank(message = "Name is required")
    @Size(min = 2, max = 50)
    private String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @Pattern(regexp = "^[6-9]\\d{9}$", message = "Phone must be valid 10-digit Indian number")
    private String phone;

    @NotBlank(message = "Password is required")
    @Size(min = 8)
    private String password;

    @NotBlank private String designation;
    @NotBlank private String department;

    private String qualification;
    private int experienceYears;
}