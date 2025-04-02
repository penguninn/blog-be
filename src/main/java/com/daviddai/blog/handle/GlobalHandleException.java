package com.daviddai.blog.handle;

import java.util.ArrayList;
import java.util.List;

import com.daviddai.blog.exceptions.AuthServiceException;
import com.daviddai.blog.exceptions.CategoryNotFoundException;
import com.daviddai.blog.exceptions.PostNotFoundException;
import com.daviddai.blog.exceptions.UserAlreadyExistException;
import com.daviddai.blog.model.dtos.response.ResponseError;
import com.daviddai.blog.model.dtos.response.ResponseSuccess;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalHandleException {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseSuccess<?> handleValidationExceptions(MethodArgumentNotValidException ex) {
        // Map<String, String> errors = new HashMap<>();
        // ex.getBindingResult().getFieldErrors()
        // .forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
        // ex.getBindingResult().getGlobalErrors()
        // .forEach(error -> errors.put(error.getObjectName(),
        // error.getDefaultMessage()));

        List<String> errors = new ArrayList<>();
        ex.getBindingResult().getFieldErrors()
                .forEach(error -> errors.add(error.getDefaultMessage()));
        ex.getBindingResult().getGlobalErrors()
                .forEach(error -> errors.add(error.getDefaultMessage()));

        return new ResponseError<>(HttpStatus.BAD_REQUEST, errors);
    }

    @ExceptionHandler(UserAlreadyExistException.class)
    public ResponseSuccess<?> handleUserAlreadyException(UserAlreadyExistException ex) {
        return new ResponseSuccess<>(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseSuccess<?> handleUsernameNotFoundException(UsernameNotFoundException ex) {
        return new ResponseSuccess<>(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(AuthServiceException.class)
    public ResponseSuccess<?> handleAuthServiceException(AuthServiceException ex) {
        return new ResponseSuccess<>(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(PostNotFoundException.class)
    public ResponseSuccess<?> handlePostNotFoundException(PostNotFoundException ex) {
        return new ResponseSuccess<>(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(CategoryNotFoundException.class)
    public ResponseSuccess<?> handleCategoryNotFoundException(CategoryNotFoundException ex) {
        return new ResponseSuccess<>(HttpStatus.NOT_FOUND, ex.getMessage());
    }

}
