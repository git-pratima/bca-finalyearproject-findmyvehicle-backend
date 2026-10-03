package com.pratima.bca.findmyvehicle.exception;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.pratima.bca.findmyvehicle.dto.Response;
import com.pratima.bca.findmyvehicle.dto.Status;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    @Autowired
    private ObjectMapper objectMapper;


    @Override
    public void handle(HttpServletRequest request,
                       HttpServletResponse response,
                       AccessDeniedException accessDeniedException)
            throws IOException, ServletException {

        Status status = Status.builder()
                .status(HttpStatus.FORBIDDEN.value())
                .message(accessDeniedException.getMessage())
                .build();

        Response errorResponse = Response.builder()
                .status(status)
                .build();


        response.setContentType("application/json");
        response.setStatus(HttpStatus.FORBIDDEN.value());
        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));

    }
}
