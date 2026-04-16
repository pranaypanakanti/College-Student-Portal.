package com.college.student.portal.entity;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

import com.college.student.portal.enums.Role;

class EntityTest {

    @Test
    void testPasswordResetToken_IsExpired() {
        PasswordResetToken token = new PasswordResetToken();
        token.setExpiryDate(LocalDateTime.now().minusHours(1));
        assertTrue(token.isExpired());
    }

    @Test
    void testPasswordResetToken_IsNotExpired() {
        PasswordResetToken token = new PasswordResetToken();
        token.setExpiryDate(LocalDateTime.now().plusMinutes(30));
        assertFalse(token.isExpired());
    }

    @Test
    void testStudent_GetterSetter() {
        Student student = new Student();
        student.setId(1);
        student.setRollNumber("CSE001");
        student.setName("Test");
        student.setEmail("test@test.com");
        student.setRole(Role.STUDENT);

        assertEquals(1, student.getId());
        assertEquals("CSE001", student.getRollNumber());
        assertEquals("Test", student.getName());
        assertEquals("test@test.com", student.getEmail());
        assertEquals(Role.STUDENT, student.getRole());
    }

    @Test
    void testFaculty_GetterSetter() {
        Faculty faculty = new Faculty();
        faculty.setId(1);
        faculty.setName("Dr. Test");
        faculty.setEmail("dr@test.com");
        faculty.setDepartment("CSE");
        faculty.setRole(Role.FACULTY);

        assertEquals(1, faculty.getId());
        assertEquals("Dr. Test", faculty.getName());
        assertEquals("CSE", faculty.getDepartment());
        assertEquals(Role.FACULTY, faculty.getRole());
    }

    @Test
    void testAdmin_GetterSetter() {
        Admin admin = new Admin();
        admin.setId(1);
        admin.setName("Admin");
        admin.setEmail("admin@test.com");
        admin.setRole(Role.ADMIN);

        assertEquals(1, admin.getId());
        assertEquals("Admin", admin.getName());
        assertEquals(Role.ADMIN, admin.getRole());
    }
}