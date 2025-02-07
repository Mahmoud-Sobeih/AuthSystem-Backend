package com.project.AuthSystem.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.AuthSystem.config.exception.ExceptionResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Date;

@Component
@Slf4j
public class AuthEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {

        log.error("EntryPoint Unauthorized Error");

        // Create the ExceptionResponse object
        ExceptionResponse exceptionResponse = new ExceptionResponse(
                new Date(),
                HttpServletResponse.SC_UNAUTHORIZED,
                "Access denied. Please provide valid authentication."
        );

        // Configure the response to return JSON
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().write(convertObjectToJson(exceptionResponse));
    }

    private String convertObjectToJson(Object object) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(object);
    }
}
