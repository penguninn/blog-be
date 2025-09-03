package com.daviddai.blog.utils;

import com.daviddai.blog.dto.response.ApiResponse;
import com.daviddai.blog.dto.response.PageResponse;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public class ApiResponseBuilder {

    public static <T> ResponseEntity<ApiResponse<T>> success(T data) {
        return success(data, HttpStatus.OK, "Operation completed successfully");
    }

    public static <T> ResponseEntity<ApiResponse<T>> success(T data, String message) {
        return success(data, HttpStatus.OK, message);
    }

    public static <T> ResponseEntity<ApiResponse<T>> success(T data, HttpStatus status, String message) {
        ApiResponse<T> response = ApiResponse.<T>builder()
                .success(true)
                .statusCode(status.value())
                .message(message)
                .data(data)
                .timestamp(Instant.now())
                .path(getCurrentPath())
                .correlationId(generateCorrelationId())
                .build();

        return ResponseEntity.status(status).body(response);
    }

    public static <T> ResponseEntity<ApiResponse<List<T>>> successWithPagination(
            List<T> data, 
            ApiResponse.PaginationMetadata pagination) {
        
        ApiResponse<List<T>> response = ApiResponse.<List<T>>builder()
                .success(true)
                .statusCode(HttpStatus.OK.value())
                .message("Data retrieved successfully")
                .data(data)
                .pagination(pagination)
                .timestamp(Instant.now())
                .path(getCurrentPath())
                .correlationId(generateCorrelationId())
                .build();

        return ResponseEntity.ok(response);
    }
    public static <T> ResponseEntity<ApiResponse<List<T>>> successWithPagination(Page<T> page) {
        ApiResponse.PaginationMetadata pagination = ApiResponse.PaginationMetadata.builder()
                .page(page.getNumber())
                .size(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .first(page.isFirst())
                .last(page.isLast())
                .hasNext(page.hasNext())
                .hasPrevious(page.hasPrevious())
                .build();

        return successWithPagination(page.getContent(), pagination);
    }

    public static <T> ResponseEntity<ApiResponse<List<T>>> successWithPagination(PageResponse<T> pageResponse) {
        ApiResponse.PaginationMetadata pagination = ApiResponse.PaginationMetadata.builder()
                .page(pageResponse.getPage())
                .size(pageResponse.getSize())
                .totalElements(pageResponse.getTotalElements())
                .totalPages(pageResponse.getTotalPages())
                .first(pageResponse.getPage() == 0)
                .last(pageResponse.getPage() == pageResponse.getTotalPages() - 1)
                .hasNext(pageResponse.getPage() < pageResponse.getTotalPages() - 1)
                .hasPrevious(pageResponse.getPage() > 0)
                .build();

        return successWithPagination(pageResponse.getContents(), pagination);
    }

    public static <T> ResponseEntity<ApiResponse<T>> created(T data) {
        return success(data, HttpStatus.CREATED, "Resource created successfully");
    }

    public static <T> ResponseEntity<ApiResponse<T>> created(T data, String message) {
        return success(data, HttpStatus.CREATED, message);
    }

    public static ResponseEntity<ApiResponse<Void>> noContent() {
        return noContent("Operation completed successfully");
    }

    public static ResponseEntity<ApiResponse<Void>> noContent(String message) {
        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .success(true)
                .statusCode(HttpStatus.NO_CONTENT.value())
                .message(message)
                .timestamp(Instant.now())
                .path(getCurrentPath())
                .correlationId(generateCorrelationId())
                .build();

        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(response);
    }

    public static ResponseEntity<ApiResponse<Void>> error(ProblemDetail problemDetail) {
        ApiResponse.ErrorDetails errorDetails = ApiResponse.ErrorDetails.builder()
                .type(problemDetail.getType() != null ? problemDetail.getType().toString() : null)
                .title(problemDetail.getTitle())
                .detail(problemDetail.getDetail())
                .instance(problemDetail.getInstance() != null ? problemDetail.getInstance().toString() : null)
                .errors(problemDetail.getProperties())
                .build();

        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .success(false)
                .statusCode(problemDetail.getStatus())
                .message(problemDetail.getTitle())
                .error(errorDetails)
                .timestamp(Instant.now())
                .path(getCurrentPath())
                .correlationId(generateCorrelationId())
                .build();

        return ResponseEntity.status(problemDetail.getStatus()).body(response);
    }

    public static ResponseEntity<ApiResponse<Void>> error(HttpStatus status, String message) {
        return error(status, message, null);
    }

    public static ResponseEntity<ApiResponse<Void>> error(HttpStatus status, String message, String detail) {
        ApiResponse.ErrorDetails errorDetails = ApiResponse.ErrorDetails.builder()
                .type("https://api.blog.com/problems/" + status.name().toLowerCase().replace('_', '-'))
                .title(message)
                .detail(detail)
                .instance(getCurrentPath())
                .build();

        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .success(false)
                .statusCode(status.value())
                .message(message)
                .error(errorDetails)
                .timestamp(Instant.now())
                .path(getCurrentPath())
                .correlationId(generateCorrelationId())
                .build();

        return ResponseEntity.status(status).body(response);
    }

    private static String getCurrentPath() {
        try {
            ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
            HttpServletRequest request = attr.getRequest();
            return request.getRequestURI();
        } catch (Exception e) {
            return "/unknown";
        }
    }

    private static String generateCorrelationId() {
        return UUID.randomUUID().toString();
    }
}