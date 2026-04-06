package com.college.student.portal.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.college.student.portal.dto.*;
import com.college.student.portal.entity.Student;
import com.college.student.portal.enums.Role;
import com.college.student.portal.repository.StudentRepository;
import com.college.student.portal.security.JwtUtil;
import com.college.student.portal.security.StudentUserDetails;

@Service
public class StudentService {

    private final StudentRepository studentRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    public StudentService(StudentRepository studentRepository, PasswordEncoder passwordEncoder,
                          JwtUtil jwtUtil, AuthenticationManager authenticationManager) {
        this.studentRepository = studentRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
    }

    // Register Student
    public ResponseEntity<Map<String, Object>> registerStudent(StudentDTO dto) {

        if (studentRepository.findByEmail(dto.getEmail()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("message", "Email already exists!"));
        }

        if (studentRepository.findByRollNumber(dto.getRollNumber()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("message", "Roll number already exists!"));
        }

        Student student = new Student();
        student.setRollNumber(dto.getRollNumber());
        student.setName(dto.getName());
        student.setEmail(dto.getEmail());
        student.setPhone(dto.getPhone());
        student.setPasswordHash(passwordEncoder.encode(dto.getPassword()));
        student.setEnrollmentYear(dto.getEnrollmentYear());
        student.setSemester(dto.getSemester());
        student.setBranch(dto.getBranch());
        student.setDob(dto.getDob());
        student.setAddress(dto.getAddress());
        student.setCity(dto.getCity());
        student.setPincode(dto.getPincode());
        student.setRole(Role.STUDENT);

        studentRepository.save(student);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of("message", "Student Registration Successful!", "role", student.getRole()));
    }

    // Login Student
    public ResponseEntity<ApiResponse<JwtLoginResponse>> loginStudent(LoginRequest loginRequest) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

            Student student = studentRepository.findByEmail(loginRequest.getEmail())
                    .orElseThrow(() -> new RuntimeException("Student not found!"));

            String token = jwtUtil.createToken(student.getEmail());

            JwtLoginResponse jwtResponse = new JwtLoginResponse(token, student.getEmail(), student.getRole());

            return ResponseEntity.ok(new ApiResponse<>(true, "Login successful!", jwtResponse));

        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiResponse<>(false, "Invalid Credentials!", null));
        }
    }

    // Get All Students
    public List<StudentResponseDTO> getAllStudents() {
        return studentRepository.findAll().stream()
                .map(s -> new StudentResponseDTO(s.getId(), s.getRollNumber(), s.getName(), s.getEmail(),
                        s.getPhone(), s.getEnrollmentYear(), s.getSemester(), s.getBranch(),
                        s.getAddress(), s.getCity(), s.getPincode()))
                .toList();
    }

    // Get Student by ID
    public StudentResponseDTO getStudentById(int id) {
        Student s = studentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Student not found with id: " + id));
        return new StudentResponseDTO(s.getId(), s.getRollNumber(), s.getName(), s.getEmail(),
                s.getPhone(), s.getEnrollmentYear(), s.getSemester(), s.getBranch(),
                s.getAddress(), s.getCity(), s.getPincode());
    }

    // Update Student
    public ResponseEntity<Map<String, Object>> updateStudent(StudentUpdateDTO dto, int id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Student not found!"));

        if (dto.getPhone() != null) student.setPhone(dto.getPhone());
        if (dto.getAddress() != null) student.setAddress(dto.getAddress());
        if (dto.getCity() != null) student.setCity(dto.getCity());
        if (dto.getPincode() != null) student.setPincode(dto.getPincode());

        studentRepository.save(student);

        return ResponseEntity.ok(Map.of("message", "Student updated successfully!"));
    }

    // Change Password
    public ResponseEntity<Map<String, Object>> changePassword(String email, ChangePasswordDTO dto) {
        Student student = studentRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Student not found!"));

        if (!passwordEncoder.matches(dto.getCurrentPassword(), student.getPasswordHash())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", "Current password is incorrect!"));
        }

        if (!dto.getNewPassword().equals(dto.getConfirmPassword())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", "New password and confirm password do not match!"));
        }

        student.setPasswordHash(passwordEncoder.encode(dto.getNewPassword()));
        studentRepository.save(student);

        return ResponseEntity.ok(Map.of("message", "Password changed successfully!"));
    }

    // Get students by branch and semester
    public List<StudentResponseDTO> getStudentsByBranchAndSemester(String branch, String semester) {
        return studentRepository.findByBranchAndSemester(branch, semester).stream()
                .map(s -> new StudentResponseDTO(s.getId(), s.getRollNumber(), s.getName(), s.getEmail(),
                        s.getPhone(), s.getEnrollmentYear(), s.getSemester(), s.getBranch(),
                        s.getAddress(), s.getCity(), s.getPincode()))
                .toList();
    }
}