package com.daviddai.blog.handle;

import java.util.ArrayList;
import java.util.List;

import com.daviddai.blog.exceptions.AuthServiceException;
import com.daviddai.blog.exceptions.CategoryNotFoundException;
import com.daviddai.blog.exceptions.JwtException;
import com.daviddai.blog.exceptions.PostNotFoundException;
import com.daviddai.blog.exceptions.UserAlreadyExistException;
import com.daviddai.blog.model.dtos.response.ResponseError;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalHandleException {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseError<?> handleValidationExceptions(MethodArgumentNotValidException ex) {
        List<String> errors = new ArrayList<>();
        ex.getBindingResult().getFieldErrors()
                .forEach(error -> errors.add(error.getDefaultMessage()));
        ex.getBindingResult().getGlobalErrors()
                .forEach(error -> errors.add(error.getDefaultMessage()));

        return new ResponseError<>(HttpStatus.BAD_REQUEST, "Invalid validation", errors);
    }

    @ExceptionHandler(JwtException.class)
    public ResponseError<?> handleJwtException(JwtException ex) {
        return new ResponseError<>(HttpStatus.UNAUTHORIZED, ex.getMessage());
    }

    @ExceptionHandler(UserAlreadyExistException.class)
    public ResponseError<?> handleUserAlreadyException(UserAlreadyExistException ex) {
        return new ResponseError<>(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseError<?> handleUsernameNotFoundException(UsernameNotFoundException ex) {
        return new ResponseError<>(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(AuthServiceException.class)
    public ResponseError<?> handleAuthServiceException(AuthServiceException ex) {
        return new ResponseError<>(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(PostNotFoundException.class)
    public ResponseError<?> handlePostNotFoundException(PostNotFoundException ex) {
        return new ResponseError<>(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(CategoryNotFoundException.class)
    public ResponseError<?> handleCategoryNotFoundException(CategoryNotFoundException ex) {
        return new ResponseError<>(HttpStatus.NOT_FOUND, ex.getMessage());
    }

}
