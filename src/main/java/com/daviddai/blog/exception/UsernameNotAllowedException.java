package com.daviddai.blog.exception;

public class UsernameNotAllowedException extends RuntimeException {
    public UsernameNotAllowedException(String message) {
        super(message);
    }
}