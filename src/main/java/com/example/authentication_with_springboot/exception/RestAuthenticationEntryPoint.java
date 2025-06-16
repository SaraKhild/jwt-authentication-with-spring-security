package com.example.authentication_with_springboot.exception;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

/*
  Custom handler for unauthorized access in Spring Security.
  It defines what happens when a user tries to access a protected resource without being authenticated (i.e., without a valid JWT or session).
*/

@Component
public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // status code to 401 Unauthorized
//        response.getWriter().write("{\"message\": \"Please log in to access this resource.\"}");
        // Custom message
        response.getWriter().write(authException.getMessage());
    }

}