package com.college.student.portal.dto;

import com.college.student.portal.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JwtLoginResponse {

    private String token;
    private String email;
    private Role role;
}