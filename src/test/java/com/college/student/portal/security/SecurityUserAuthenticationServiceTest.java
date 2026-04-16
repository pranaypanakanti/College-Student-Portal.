package com.college.student.portal.security;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.college.student.portal.entity.Admin;
import com.college.student.portal.entity.Faculty;
import com.college.student.portal.entity.Student;
import com.college.student.portal.enums.Role;
import com.college.student.portal.repository.AdminRepository;
import com.college.student.portal.repository.FacultyRepository;
import com.college.student.portal.repository.StudentRepository;

@ExtendWith(MockitoExtension.class)
class SecurityUserAuthenticationServiceTest {

    @Mock private StudentRepository studentRepository;
    @Mock private FacultyRepository facultyRepository;
    @Mock private AdminRepository adminRepository;

    @InjectMocks private SecurityUserAuthenticationService service;

    @Test
    void testLoadByUsername_FindsStudent() {
        Student student = new Student();
        student.setEmail("student@test.com");
        student.setPasswordHash("hash");
        student.setRole(Role.STUDENT);

        when(studentRepository.findByEmail("student@test.com")).thenReturn(Optional.of(student));

        UserDetails details = service.loadUserByUsername("student@test.com");
        assertInstanceOf(StudentUserDetails.class, details);
        assertEquals("student@test.com", details.getUsername());
    }

    @Test
    void testLoadByUsername_FindsFaculty() {
        Faculty faculty = new Faculty();
        faculty.setEmail("faculty@test.com");
        faculty.setPasswordHash("hash");
        faculty.setRole(Role.FACULTY);

        when(studentRepository.findByEmail("faculty@test.com")).thenReturn(Optional.empty());
        when(facultyRepository.findByEmail("faculty@test.com")).thenReturn(Optional.of(faculty));

        UserDetails details = service.loadUserByUsername("faculty@test.com");
        assertInstanceOf(FacultyUserDetails.class, details);
    }

    @Test
    void testLoadByUsername_FindsAdmin() {
        Admin admin = new Admin();
        admin.setEmail("admin@test.com");
        admin.setPasswordHash("hash");
        admin.setRole(Role.ADMIN);

        when(studentRepository.findByEmail("admin@test.com")).thenReturn(Optional.empty());
        when(facultyRepository.findByEmail("admin@test.com")).thenReturn(Optional.empty());
        when(adminRepository.findByEmail("admin@test.com")).thenReturn(Optional.of(admin));

        UserDetails details = service.loadUserByUsername("admin@test.com");
        assertInstanceOf(AdminUserDetails.class, details);
    }

    @Test
    void testLoadByUsername_NotFound() {
        when(studentRepository.findByEmail("unknown@test.com")).thenReturn(Optional.empty());
        when(facultyRepository.findByEmail("unknown@test.com")).thenReturn(Optional.empty());
        when(adminRepository.findByEmail("unknown@test.com")).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class,
                () -> service.loadUserByUsername("unknown@test.com"));
    }
}