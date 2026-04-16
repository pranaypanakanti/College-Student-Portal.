package com.college.student.portal.security;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class JwtUtilTest {

    private final JwtUtil jwtUtil = new JwtUtil();

    @Test
    void testCreateToken_ReturnsNonNullToken() {
        String token = jwtUtil.createToken("test@college.com");
        assertNotNull(token);
        assertFalse(token.isEmpty());
    }

    @Test
    void testGetUsernameFromToken() {
        String token = jwtUtil.createToken("test@college.com");
        String username = jwtUtil.getUsernameFromToken(token);
        assertEquals("test@college.com", username);
    }

    @Test
    void testIsTokenExpired_FreshToken() {
        String token = jwtUtil.createToken("test@college.com");
        assertFalse(jwtUtil.isTokenExpired(token));
    }

    @Test
    void testIsValidToken_CorrectUsername() {
        String token = jwtUtil.createToken("test@college.com");
        assertTrue(jwtUtil.isValidToken(token, "test@college.com"));
    }

    @Test
    void testIsValidToken_WrongUsername() {
        String token = jwtUtil.createToken("test@college.com");
        assertFalse(jwtUtil.isValidToken(token, "wrong@college.com"));
    }

    @Test
    void testIsValidToken_CaseInsensitive() {
        String token = jwtUtil.createToken("Test@College.com");
        assertTrue(jwtUtil.isValidToken(token, "test@college.com"));
    }

    @Test
    void testDifferentUsersGetDifferentTokens() {
        String token1 = jwtUtil.createToken("user1@college.com");
        String token2 = jwtUtil.createToken("user2@college.com");
        assertNotEquals(token1, token2);
    }
}