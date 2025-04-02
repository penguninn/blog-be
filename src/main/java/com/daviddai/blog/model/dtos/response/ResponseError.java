package com.daviddai.blog.model.dtos.response;

import org.springframework.http.HttpStatusCode;

public class ResponseError<T> extends ResponseSuccess<T> {

    public ResponseError(HttpStatusCode status, T error) {
        super(status, error);
    }
}
