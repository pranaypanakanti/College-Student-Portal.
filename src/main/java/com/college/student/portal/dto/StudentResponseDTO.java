package com.college.student.portal.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentResponseDTO {

    private Integer id;
    private String rollNumber;
    private String name;
    private String email;
    private String phone;
    private int enrollmentYear;
    private String semester;
    private String branch;
    private String address;
    private String city;
    private String pincode;
}