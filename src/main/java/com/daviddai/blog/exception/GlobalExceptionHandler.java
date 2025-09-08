package com.daviddai.blog.exception;

import com.daviddai.blog.dto.response.ApiResponse;
import com.daviddai.blog.utils.ApiResponseBuilder;
import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.server.ResponseStatusException;

import java.net.URI;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(PostNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handlePostNotFound(PostNotFoundException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
        problemDetail.setType(URI.create("https://api.blog.com/problems/post-not-found"));
        problemDetail.setTitle("Post Not Found");
        return ApiResponseBuilder.error(problemDetail);
    }

    @ExceptionHandler(CategoryNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleCategoryNotFound(CategoryNotFoundException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
        problemDetail.setType(URI.create("https://api.blog.com/problems/category-not-found"));
        problemDetail.setTitle("Category Not Found");
        return ApiResponseBuilder.error(problemDetail);
    }


    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleUserNotFound(UserNotFoundException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
        problemDetail.setType(URI.create("https://api.blog.com/problems/user-not-found"));
        problemDetail.setTitle("User Not Found");
        return ApiResponseBuilder.error(problemDetail);
    }

    @ExceptionHandler(UsernameNotAllowedException.class)
    public ProblemDetail handleUsernameNotAllowed(UsernameNotAllowedException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getMessage());
        problemDetail.setType(URI.create("https://api.blog.com/problems/username-not-allowed"));
        problemDetail.setTitle("Username Change Not Allowed");
        return problemDetail;
    }

    @ExceptionHandler(UserAlreadyExistException.class)
    public ProblemDetail handleUserAlreadyExists(UserAlreadyExistException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, ex.getMessage());
        problemDetail.setType(URI.create("https://api.blog.com/problems/user-already-exists"));
        problemDetail.setTitle("User Already Exists");
        return problemDetail;
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ProblemDetail handleAccessDenied(AccessDeniedException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.FORBIDDEN, "Access denied");
        problemDetail.setType(URI.create("https://api.blog.com/problems/access-denied"));
        problemDetail.setTitle("Access Denied");
        return problemDetail;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Void>> handleValidationError(MethodArgumentNotValidException ex) {
        Map<String, String> fieldErrors = new LinkedHashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                fieldErrors.put(error.getField(), error.getDefaultMessage()));
        
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "Validation failed");
        problemDetail.setType(URI.create("https://api.blog.com/problems/validation-error"));
        problemDetail.setTitle("Validation Error");
        problemDetail.setProperty("errors", fieldErrors);
        
        return ApiResponseBuilder.error(problemDetail);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ProblemDetail handleConstraintViolation(ConstraintViolationException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "Constraint validation failed");
        problemDetail.setType(URI.create("https://api.blog.com/problems/constraint-violation"));
        problemDetail.setTitle("Constraint Violation");
        
        String violations = ex.getConstraintViolations().stream()
                .map(violation -> violation.getPropertyPath() + ": " + violation.getMessage())
                .collect(Collectors.joining(", "));
        problemDetail.setProperty("violations", violations);
        
        return problemDetail;
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ProblemDetail handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST,
                String.format("Invalid parameter '%s': expected %s", ex.getName(), ex.getRequiredType().getSimpleName()));
        problemDetail.setType(URI.create("https://api.blog.com/problems/type-mismatch"));
        problemDetail.setTitle("Parameter Type Mismatch");
        return problemDetail;
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ProblemDetail handleIllegalArgument(IllegalArgumentException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getMessage());
        problemDetail.setType(URI.create("https://api.blog.com/problems/illegal-argument"));
        problemDetail.setTitle("Illegal Argument");
        return problemDetail;
    }

    @ExceptionHandler(IllegalStateException.class)
    public ProblemDetail handleIllegalState(IllegalStateException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, ex.getMessage());
        problemDetail.setType(URI.create("https://api.blog.com/problems/illegal-state"));
        problemDetail.setTitle("Illegal State");
        return problemDetail;
    }

    @ExceptionHandler(CommentNotFoundException.class)
    public ProblemDetail handleCommentNotFound(CommentNotFoundException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
        problemDetail.setType(URI.create("https://api.blog.com/problems/comment-not-found"));
        problemDetail.setTitle("Comment Not Found");
        return problemDetail;
    }

    @ExceptionHandler(AssetNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleAssetNotFound(AssetNotFoundException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
        problemDetail.setType(URI.create("https://api.blog.com/problems/asset-not-found"));
        problemDetail.setTitle("Asset Not Found");
        return ApiResponseBuilder.error(problemDetail);
    }

    @ExceptionHandler(AssetUploadException.class)
    public ResponseEntity<ApiResponse<Void>> handleAssetUpload(AssetUploadException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getMessage());
        problemDetail.setType(URI.create("https://api.blog.com/problems/asset-upload-error"));
        problemDetail.setTitle("Asset Upload Error");
        return ApiResponseBuilder.error(problemDetail);
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<ApiResponse<Void>> handleMaxUploadSize(MaxUploadSizeExceededException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
            HttpStatus.PAYLOAD_TOO_LARGE, 
            "File size cannot exceed"
        );
        problemDetail.setType(URI.create("https://api.blog.com/problems/file-too-large"));
        problemDetail.setTitle("File Too Large");
        return ApiResponseBuilder.error(problemDetail);
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ProblemDetail handleResponseStatusException(ResponseStatusException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(ex.getStatusCode(), ex.getReason());
        problemDetail.setType(URI.create("https://api.blog.com/problems/response-status-error"));
        problemDetail.setTitle("Response Status Error");
        return problemDetail;
    }

    @ExceptionHandler(DuplicateKeyException.class)
    public ProblemDetail handleDuplicateKey(DuplicateKeyException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT,
                "Resource already exists with this unique identifier");
        problemDetail.setType(URI.create("https://api.blog.com/problems/duplicate-key"));
        problemDetail.setTitle("Duplicate Key");
        return problemDetail;
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ProblemDetail handleDataIntegrityViolation(DataIntegrityViolationException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST,
                "Data integrity constraint violation");
        problemDetail.setType(URI.create("https://api.blog.com/problems/data-integrity"));
        problemDetail.setTitle("Data Integrity Violation");
        return problemDetail;
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ProblemDetail handleHttpMessageNotReadable(HttpMessageNotReadableException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST,
                "Malformed JSON request");
        problemDetail.setType(URI.create("https://api.blog.com/problems/malformed-json"));
        problemDetail.setTitle("Malformed JSON");
        return problemDetail;
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ProblemDetail handleMissingParameter(MissingServletRequestParameterException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST,
                String.format("Required parameter '%s' is missing", ex.getParameterName()));
        problemDetail.setType(URI.create("https://api.blog.com/problems/missing-parameter"));
        problemDetail.setTitle("Missing Parameter");
        return problemDetail;
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ProblemDetail handleMethodNotSupported(HttpRequestMethodNotSupportedException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.METHOD_NOT_ALLOWED,
                String.format("HTTP method '%s' is not supported for this endpoint", ex.getMethod()));
        problemDetail.setType(URI.create("https://api.blog.com/problems/method-not-supported"));
        problemDetail.setTitle("Method Not Supported");
        return problemDetail;
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleGenericException(Exception ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR,
                "An unexpected error occurred");
        problemDetail.setType(URI.create("https://api.blog.com/problems/internal-error"));
        problemDetail.setTitle("Internal Server Error");
        return ApiResponseBuilder.error(problemDetail);
    }
}
