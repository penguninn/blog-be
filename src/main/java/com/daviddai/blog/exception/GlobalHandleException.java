package com.daviddai.blog.exception;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.daviddai.blog.enums.AppCode;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalHandleException {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ProblemDetail> handleValidateException(MethodArgumentNotValidException ex) {
        Map<String, String> fieldErrors = new LinkedHashMap<>();
        ex.getBindingResult().getFieldErrors()
                .forEach(error -> fieldErrors.put(error.getField(), error.getDefaultMessage()));
        AppCode appCode = AppCode.VALIDATION_ERROR;
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(
                appCode.getHttpStatus(),
                appCode.getMessage());
        pd.setProperty("fieldErrors", ex.getTitleMessageCode());
        pd.setProperty("code", appCode);
        pd.setProperty("timestamp", Instant.now());
        return ResponseEntity
                .status(AppCode.VALIDATION_ERROR.getHttpStatus())
                .body(pd);
    }

    @ExceptionHandler(AppException.class)
    public ResponseEntity<ProblemDetail> handleAppException(AppException ex) {
        AppCode appCode = ex.getAppCode();
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(
                appCode.getHttpStatus(),
                appCode.getMessage());
        pd.setProperty("code", appCode);
        pd.setProperty("timestamp", Instant.now());
        return ResponseEntity
                .status(appCode.getHttpStatus())
                .body(pd);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ProblemDetail> handleUnknow(Exception ex) {
        log.error("Unhandled exception", ex);
        return ResponseEntity
                .status(500)
                .body(null);
    }
}
