package com.college.student.portal.security;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.college.student.portal.entity.Admin;
import com.college.student.portal.entity.Faculty;
import com.college.student.portal.entity.Student;
import com.college.student.portal.enums.Role;

class UserDetailsTest {


    @Test
    void testStudentUserDetails_Authorities() {
        Student student = new Student();
        student.setEmail("student@test.com");
        student.setPasswordHash("hash");
        student.setRole(Role.STUDENT);

        StudentUserDetails details = new StudentUserDetails(student);

        assertEquals("student@test.com", details.getUsername());
        assertEquals("hash", details.getPassword());
        assertEquals(1, details.getAuthorities().size());
        assertTrue(details.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_STUDENT")));
        assertTrue(details.isAccountNonExpired());
        assertTrue(details.isAccountNonLocked());
        assertTrue(details.isCredentialsNonExpired());
        assertTrue(details.isEnabled());
        assertEquals(student, details.getStudent());
    }


    @Test
    void testFacultyUserDetails_Authorities() {
        Faculty faculty = new Faculty();
        faculty.setEmail("faculty@test.com");
        faculty.setPasswordHash("hash");
        faculty.setRole(Role.FACULTY);

        FacultyUserDetails details = new FacultyUserDetails(faculty);

        assertEquals("faculty@test.com", details.getUsername());
        assertEquals("hash", details.getPassword());
        assertEquals(1, details.getAuthorities().size());
        assertTrue(details.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_FACULTY")));
        assertTrue(details.isAccountNonExpired());
        assertTrue(details.isAccountNonLocked());
        assertTrue(details.isCredentialsNonExpired());
        assertTrue(details.isEnabled());
        assertEquals(faculty, details.getFaculty());
    }


    @Test
    void testAdminUserDetails_Authorities() {
        Admin admin = new Admin();
        admin.setEmail("admin@test.com");
        admin.setPasswordHash("hash");
        admin.setRole(Role.ADMIN);

        AdminUserDetails details = new AdminUserDetails(admin);

        assertEquals("admin@test.com", details.getUsername());
        assertEquals("hash", details.getPassword());
        assertEquals(1, details.getAuthorities().size());
        assertTrue(details.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN")));
        assertTrue(details.isAccountNonExpired());
        assertTrue(details.isAccountNonLocked());
        assertTrue(details.isCredentialsNonExpired());
        assertTrue(details.isEnabled());
        assertEquals(admin, details.getAdmin());
    }
}