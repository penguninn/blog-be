package com.daviddai.blog.enums;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AppCode {

    // --- Success Codes ---
    OK(null, HttpStatus.OK),
    CREATED("Create successfully", HttpStatus.CREATED),

    // --- Error Codes ---
    // Common
    VALIDATION_ERROR("Input is invalid", HttpStatus.BAD_REQUEST),
    RATE_LIMIT_EXCEEDED("Too many requests per minute", HttpStatus.TOO_MANY_REQUESTS),
    UNCATEGORIZED_EXCEPTION("Uncategorized error", HttpStatus.INTERNAL_SERVER_ERROR),

    // Auth / User
    EMAIL_ALREADY_EXISTS("Email is already registered", HttpStatus.CONFLICT),
    INVALID_CREDENTIALS("Email or password is incorrect", HttpStatus.UNAUTHORIZED),
    TOKEN_EXPIRED("Token is expired", HttpStatus.UNAUTHORIZED),
    INVALID_TOKEN("Token is invalid or revoked", HttpStatus.UNAUTHORIZED),
    FORBIDDEN("User does not have permission", HttpStatus.FORBIDDEN),
    USER_NOT_FOUND("User does not exist", HttpStatus.NOT_FOUND),
    UNAUTHENTICATED("Authentication is required", HttpStatus.UNAUTHORIZED),

    // Post
    POST_NOT_FOUND("Post does not exist or has been deleted", HttpStatus.NOT_FOUND),
    POST_ALREADY_PUBLISHED("Post is already published", HttpStatus.CONFLICT),

    // Comment
    COMMENT_NOT_FOUND("Comment does not exist", HttpStatus.NOT_FOUND),
    PARENT_COMMENT_NOT_FOUND("Parent comment does not exist", HttpStatus.NOT_FOUND),

    // Category
    CATEGORY_NOT_FOUND("Category does not exist", HttpStatus.NOT_FOUND),
    CATEGORY_IN_USE("Category still has assigned posts", HttpStatus.CONFLICT),

    // Tag
    TAG_NOT_FOUND("Tag does not exist", HttpStatus.NOT_FOUND),
    TAG_ALREADY_EXISTS("Tag already exists", HttpStatus.CONFLICT);

    private final String message;
    private final HttpStatus httpStatus;
}
