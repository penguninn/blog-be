package com.daviddai.blog.exception;

public class UserAlreadyExistException extends RuntimeException {

    public UserAlreadyExistException() {
        super("user already exists");
    }

    public UserAlreadyExistException(String message) {
        super(message);
    }
}
