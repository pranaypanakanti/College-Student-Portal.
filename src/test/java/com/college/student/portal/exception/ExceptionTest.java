package com.college.student.portal.exception;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class ExceptionTest {

    @Test
    void testResourceNotFoundException() {
        ResourceNotFoundException ex = new ResourceNotFoundException("Not found");
        assertEquals("Not found", ex.getMessage());
    }

    @Test
    void testDuplicateResourceException() {
        DuplicateResourceException ex = new DuplicateResourceException("Duplicate");
        assertEquals("Duplicate", ex.getMessage());
    }

    @Test
    void testUnauthorizedException() {
        UnauthorizedException ex = new UnauthorizedException("Unauthorized");
        assertEquals("Unauthorized", ex.getMessage());
    }

    @Test
    void testBadRequestException() {
        BadRequestException ex = new BadRequestException("Bad request");
        assertEquals("Bad request", ex.getMessage());
    }
}