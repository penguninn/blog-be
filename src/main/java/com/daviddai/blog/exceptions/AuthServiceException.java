package com.daviddai.blog.exceptions;

public class AuthServiceException extends RuntimeException {

    public AuthServiceException(String message) {
        super(message);
    }
}
