package com.daviddai.blog.exception;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    // Common
    VALIDATION_ERROR(1000, "Input is invalid", HttpStatus.BAD_REQUEST),
    RATE_LIMIT_EXCEEDED(1001, "Too many requests per minute", HttpStatus.TOO_MANY_REQUESTS),
    UNCATEGORIZED_EXCEPTION(9999, "Uncategorized error", HttpStatus.INTERNAL_SERVER_ERROR),

    // Auth / User
    EMAIL_ALREADY_EXISTS(2000, "Email is already registered", HttpStatus.CONFLICT),
    INVALID_CREDENTIALS(2001, "Email or password is incorrect", HttpStatus.UNAUTHORIZED),
    TOKEN_EXPIRED(2002, "Access token is expired", HttpStatus.UNAUTHORIZED),
    INVALID_TOKEN(2003, "Token is invalid or revoked", HttpStatus.UNAUTHORIZED),
    FORBIDDEN(2004, "User does not have permission", HttpStatus.FORBIDDEN),
    USER_NOT_FOUND(2005, "User does not exist", HttpStatus.NOT_FOUND),
    UNAUTHENTICATED(2006, "Authentication is required", HttpStatus.UNAUTHORIZED),

    // Post
    POST_NOT_FOUND(3000, "Post does not exist or has been deleted", HttpStatus.NOT_FOUND),
    POST_ALREADY_PUBLISHED(3002, "Post is already published", HttpStatus.CONFLICT),

    // Comment
    COMMENT_NOT_FOUND(4000, "Comment does not exist", HttpStatus.NOT_FOUND),
    PARENT_COMMENT_NOT_FOUND(4002, "Parent comment does not exist", HttpStatus.NOT_FOUND),

    // Category
    CATEGORY_NOT_FOUND(5000, "Category does not exist", HttpStatus.NOT_FOUND),
    CATEGORY_IN_USE(5001, "Category still has assigned posts", HttpStatus.CONFLICT),

    // Tag
    TAG_NOT_FOUND(6000, "Tag does not exist", HttpStatus.NOT_FOUND),
    TAG_ALREADY_EXISTS(6001, "Tag already exists", HttpStatus.CONFLICT);

    private final int code;
    private final String message;
    private final HttpStatus httpStatus;
}
