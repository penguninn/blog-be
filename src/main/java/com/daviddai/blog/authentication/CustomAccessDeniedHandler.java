package com.daviddai.blog.authentication;

import java.io.IOException;
import java.time.Instant;

import org.springframework.http.ProblemDetail;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import com.daviddai.blog.enums.AppCode;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    private final ObjectMapper objectMapper;

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
            AccessDeniedException accessDeniedException) throws IOException, ServletException {
        AppCode appCode = AppCode.FORBIDDEN;
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(
                appCode.getHttpStatus(),
                appCode.getMessage());
        pd.setProperty("code", appCode);
        pd.setProperty("timestamp", Instant.now());
        objectMapper.writeValue(response.getWriter(), pd);
    }

}
