package com.daviddai.blog.exceptions;

public class RoleNotFoundException extends RuntimeException {

    public RoleNotFoundException() {
        super("role not found");
    }

    public RoleNotFoundException(String message) {
        super(message);
    }
}
