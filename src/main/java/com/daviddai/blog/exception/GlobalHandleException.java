package com.daviddai.blog.exception;

import java.net.URI;
import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class GlobalHandleException {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ProblemDetail> handleValidateException(
            MethodArgumentNotValidException ex,
            HttpServletRequest request) {
        Map<String, String> fieldErrors = new LinkedHashMap<>();
        ex.getBindingResult().getFieldErrors()
                .forEach(error -> fieldErrors.put(error.getField(), error.getDefaultMessage()));
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                ex.getStatusCode(),
                "Validation false");
        problemDetail.setTitle(ex.getTitleMessageCode());
        problemDetail.setInstance(URI.create(request.getRequestURI()));
        problemDetail.setProperty("code", ex.getStatusCode());
        problemDetail.setProperty("error", ex.getTitleMessageCode());
        problemDetail.setProperty("timestamp", Instant.now());
        return ResponseEntity
                .status(ex.getStatusCode())
                .body(problemDetail);
    }

    @ExceptionHandler(AppException.class)
    public ResponseEntity<ProblemDetail> handleAppException(
            AppException ex,
            HttpServletRequest request) {
        ErrorCode errorCode = ex.getErrorCode();

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                errorCode.getHttpStatus(),
                errorCode.getMessage());
        problemDetail.setTitle(errorCode.name());
        problemDetail.setInstance(URI.create(request.getRequestURI()));
        problemDetail.setProperty("code", errorCode.getCode());
        problemDetail.setProperty("error", errorCode.name());
        problemDetail.setProperty("timestamp", Instant.now());
        return ResponseEntity
                .status(errorCode.getHttpStatus())
                .body(problemDetail);
    }
}
