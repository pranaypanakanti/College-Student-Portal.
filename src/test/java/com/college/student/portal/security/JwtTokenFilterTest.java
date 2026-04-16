package com.college.student.portal.security;

import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.context.SecurityContextHolder;

import com.college.student.portal.entity.Student;
import com.college.student.portal.enums.Role;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@ExtendWith(MockitoExtension.class)
class JwtTokenFilterTest {

    @Mock private JwtUtil jwtUtil;
    @Mock private SecurityUserAuthenticationService userService;
    @Mock private HttpServletRequest request;
    @Mock private HttpServletResponse response;
    @Mock private FilterChain filterChain;

    @InjectMocks private JwtTokenFilter jwtTokenFilter;

    @BeforeEach
    void setUp() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void testDoFilter_NoAuthHeader() throws Exception {
        when(request.getHeader("Authorization")).thenReturn(null);

        jwtTokenFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
        verify(jwtUtil, never()).getUsernameFromToken(any());
    }

    @Test
    void testDoFilter_InvalidHeaderFormat() throws Exception {
        when(request.getHeader("Authorization")).thenReturn("Basic sometoken");

        jwtTokenFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
        verify(jwtUtil, never()).getUsernameFromToken(any());
    }

    @Test
    void testDoFilter_ValidToken() throws Exception {
        Student student = new Student();
        student.setEmail("test@college.com");
        student.setPasswordHash("hash");
        student.setRole(Role.STUDENT);
        StudentUserDetails userDetails = new StudentUserDetails(student);

        when(request.getHeader("Authorization")).thenReturn("Bearer valid-token");
        when(jwtUtil.getUsernameFromToken("valid-token")).thenReturn("test@college.com");
        when(userService.loadUserByUsername("test@college.com")).thenReturn(userDetails);
        when(jwtUtil.isValidToken("valid-token", "test@college.com")).thenReturn(true);

        jwtTokenFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
        verify(jwtUtil).isValidToken("valid-token", "test@college.com");
    }

    @Test
    void testDoFilter_ExpiredToken() throws Exception {
        Student student = new Student();
        student.setEmail("test@college.com");
        student.setPasswordHash("hash");
        student.setRole(Role.STUDENT);
        StudentUserDetails userDetails = new StudentUserDetails(student);

        when(request.getHeader("Authorization")).thenReturn("Bearer expired-token");
        when(jwtUtil.getUsernameFromToken("expired-token")).thenReturn("test@college.com");
        when(userService.loadUserByUsername("test@college.com")).thenReturn(userDetails);
        when(jwtUtil.isValidToken("expired-token", "test@college.com")).thenReturn(false);

        jwtTokenFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
    }

    @Test
    void testDoFilter_MalformedToken() throws Exception {
        when(request.getHeader("Authorization")).thenReturn("Bearer malformed-token");
        when(jwtUtil.getUsernameFromToken("malformed-token")).thenThrow(new RuntimeException("Malformed"));

        jwtTokenFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
    }
}