package com.daviddai.blog.exception;

import com.daviddai.blog.enums.AppCode;

import lombok.Getter;

@Getter
public class AppException extends RuntimeException {

    private final AppCode appCode;

    public AppException(AppCode appCode) {
        super(appCode.getMessage());
        this.appCode = appCode;
    }
}
