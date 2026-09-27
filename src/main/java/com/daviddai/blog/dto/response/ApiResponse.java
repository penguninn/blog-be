package com.daviddai.blog.dto.response;

import java.time.Instant;

import com.daviddai.blog.enums.AppCode;
import com.fasterxml.jackson.annotation.JsonInclude;

public record ApiResponse<T>(

        AppCode code,

        String message,

        T data,

        Instant timestamp) {

    public static <T> ApiResponse<T> ok(T data) {
        return new ApiResponse<T>(AppCode.OK, null, data, Instant.now());
    }

    public static <T> ApiResponse<T> ok(AppCode appCode, T data) {
        return new ApiResponse<T>(appCode, appCode.getMessage(), data, Instant.now());
    }
}
