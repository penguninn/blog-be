package com.daviddai.blog.dto.response;

import java.io.Serializable;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ApiResponse<T> implements Serializable {
    private int code;
    private String message;
    private T data;
}
