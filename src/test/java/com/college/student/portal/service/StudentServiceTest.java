package com.college.student.portal.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.college.student.portal.dto.*;
import com.college.student.portal.entity.Student;
import com.college.student.portal.enums.Role;
import com.college.student.portal.repository.StudentRepository;
import com.college.student.portal.security.JwtUtil;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

    @Mock private StudentRepository studentRepository;
    @Mock private PasswordEncoder passwordEncoder;
    @Mock private JwtUtil jwtUtil;
    @Mock private AuthenticationManager authenticationManager;

    @InjectMocks private StudentService studentService;

    private StudentDTO studentDTO;
    private Student student;

    @BeforeEach
    void setUp() {
        studentDTO = new StudentDTO();
        studentDTO.setRollNumber("CSE2025001");
        studentDTO.setName("Test Student");
        studentDTO.setEmail("test@college.com");
        studentDTO.setPhone("9876543210");
        studentDTO.setPassword("password123");
        studentDTO.setEnrollmentYear(2025);
        studentDTO.setSemester("1");
        studentDTO.setBranch("CSE");
        studentDTO.setAddress("Test Address");
        studentDTO.setCity("Hyderabad");
        studentDTO.setPincode("500001");

        student = new Student();
        student.setId(1);
        student.setRollNumber("CSE2025001");
        student.setName("Test Student");
        student.setEmail("test@college.com");
        student.setPhone("9876543210");
        student.setPasswordHash("encodedPassword");
        student.setEnrollmentYear(2025);
        student.setSemester("1");
        student.setBranch("CSE");
        student.setAddress("Test Address");
        student.setCity("Hyderabad");
        student.setPincode("500001");
        student.setRole(Role.STUDENT);
    }

    @Test
    void testRegisterStudent_Success() {
        when(studentRepository.findByEmail(any())).thenReturn(Optional.empty());
        when(studentRepository.findByRollNumber(any())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(any())).thenReturn("encodedPassword");
        when(studentRepository.save(any())).thenReturn(student);

        ResponseEntity<?> response = studentService.registerStudent(studentDTO);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        verify(studentRepository, times(1)).save(any(Student.class));
    }

    @Test
    void testRegisterStudent_DuplicateEmail() {
        when(studentRepository.findByEmail(any())).thenReturn(Optional.of(student));

        ResponseEntity<?> response = studentService.registerStudent(studentDTO);
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        verify(studentRepository, never()).save(any());
    }

    @Test
    void testRegisterStudent_DuplicateRollNumber() {
        when(studentRepository.findByEmail(any())).thenReturn(Optional.empty());
        when(studentRepository.findByRollNumber(any())).thenReturn(Optional.of(student));

        ResponseEntity<?> response = studentService.registerStudent(studentDTO);
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        verify(studentRepository, never()).save(any());
    }

    @Test
    void testLoginStudent_Success() {
        LoginRequest loginRequest = new LoginRequest("test@college.com", "password123");

        when(authenticationManager.authenticate(any())).thenReturn(
                new UsernamePasswordAuthenticationToken("test@college.com", "password123"));
        when(studentRepository.findByEmail("test@college.com")).thenReturn(Optional.of(student));
        when(jwtUtil.createToken("test@college.com")).thenReturn("mock-jwt-token");

        ResponseEntity<ApiResponse<JwtLoginResponse>> response = studentService.loginStudent(loginRequest);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().isSuccess());
        assertEquals("mock-jwt-token", response.getBody().getData().getToken());
        assertEquals(Role.STUDENT, response.getBody().getData().getRole());
    }

    @Test
    void testLoginStudent_BadCredentials() {
        LoginRequest loginRequest = new LoginRequest("test@college.com", "wrongpassword");

        when(authenticationManager.authenticate(any())).thenThrow(new BadCredentialsException("Bad"));

        ResponseEntity<ApiResponse<JwtLoginResponse>> response = studentService.loginStudent(loginRequest);
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertFalse(response.getBody().isSuccess());
    }

    @Test
    void testGetAllStudents() {
        when(studentRepository.findAll()).thenReturn(List.of(student));

        List<StudentResponseDTO> result = studentService.getAllStudents();
        assertEquals(1, result.size());
        assertEquals("CSE2025001", result.get(0).getRollNumber());
    }

    @Test
    void testGetStudentById_Success() {
        when(studentRepository.findById(1)).thenReturn(Optional.of(student));

        StudentResponseDTO result = studentService.getStudentById(1);
        assertEquals("CSE2025001", result.getRollNumber());
        assertEquals("test@college.com", result.getEmail());
    }

    @Test
    void testGetStudentById_NotFound() {
        when(studentRepository.findById(999)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> studentService.getStudentById(999));
    }

    @Test
    void testUpdateStudent_Success() {
        StudentUpdateDTO updateDTO = new StudentUpdateDTO();
        updateDTO.setPhone("8765432109");
        updateDTO.setAddress("New Address");
        updateDTO.setCity("Mumbai");
        updateDTO.setPincode("400001");

        when(studentRepository.findById(1)).thenReturn(Optional.of(student));
        when(studentRepository.save(any())).thenReturn(student);

        ResponseEntity<?> response = studentService.updateStudent(updateDTO, 1);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(studentRepository).save(any(Student.class));
    }

    @Test
    void testUpdateStudent_PartialUpdate() {
        StudentUpdateDTO updateDTO = new StudentUpdateDTO();
        updateDTO.setPhone("8765432109");
        // address, city, pincode are null — should not be updated

        when(studentRepository.findById(1)).thenReturn(Optional.of(student));
        when(studentRepository.save(any())).thenReturn(student);

        ResponseEntity<?> response = studentService.updateStudent(updateDTO, 1);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testUpdateStudent_NotFound() {
        StudentUpdateDTO updateDTO = new StudentUpdateDTO();
        when(studentRepository.findById(999)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> studentService.updateStudent(updateDTO, 999));
    }

    @Test
    void testChangePassword_Success() {
        ChangePasswordDTO dto = new ChangePasswordDTO("password123", "newpass456", "newpass456");

        when(studentRepository.findByEmail("test@college.com")).thenReturn(Optional.of(student));
        when(passwordEncoder.matches("password123", "encodedPassword")).thenReturn(true);
        when(passwordEncoder.encode("newpass456")).thenReturn("newEncodedPassword");

        ResponseEntity<?> response = studentService.changePassword("test@college.com", dto);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(studentRepository).save(any(Student.class));
    }

    @Test
    void testChangePassword_WrongCurrentPassword() {
        ChangePasswordDTO dto = new ChangePasswordDTO("wrongcurrent", "newpass456", "newpass456");

        when(studentRepository.findByEmail("test@college.com")).thenReturn(Optional.of(student));
        when(passwordEncoder.matches("wrongcurrent", "encodedPassword")).thenReturn(false);

        ResponseEntity<?> response = studentService.changePassword("test@college.com", dto);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void testChangePassword_Mismatch() {
        ChangePasswordDTO dto = new ChangePasswordDTO("password123", "newpass456", "different789");

        when(studentRepository.findByEmail("test@college.com")).thenReturn(Optional.of(student));
        when(passwordEncoder.matches("password123", "encodedPassword")).thenReturn(true);

        ResponseEntity<?> response = studentService.changePassword("test@college.com", dto);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void testChangePassword_StudentNotFound() {
        ChangePasswordDTO dto = new ChangePasswordDTO("pass", "new", "new");

        when(studentRepository.findByEmail("unknown@test.com")).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> studentService.changePassword("unknown@test.com", dto));
    }

    @Test
    void testGetStudentsByBranchAndSemester() {
        when(studentRepository.findByBranchAndSemester("CSE", "1")).thenReturn(List.of(student));

        List<StudentResponseDTO> result = studentService.getStudentsByBranchAndSemester("CSE", "1");
        assertEquals(1, result.size());
        assertEquals("CSE", result.get(0).getBranch());
    }
}