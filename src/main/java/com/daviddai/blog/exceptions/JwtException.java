package com.daviddai.blog.exceptions;

public class JwtException extends RuntimeException {

    public JwtException(String message) {
        super(message);
    }
}
