package com.daviddai.blog.model.dtos.response;

import com.fasterxml.jackson.annotation.JsonInclude;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

public class ResponseSuccess<T> extends ResponseEntity<ResponseSuccess.Payload<T>> {

    public ResponseSuccess(HttpStatusCode status, String message) {
        super(new Payload<>(status.value(), message), HttpStatus.OK);
    }

    public ResponseSuccess(HttpStatusCode status, T error) {
        super(new Payload<>(status.value(), error), HttpStatus.OK);
    }

    public ResponseSuccess(HttpStatusCode status, String message, T data) {
        super(new Payload<>(status.value(), message, data), HttpStatus.OK);
    }

    public static class Payload<T> {
        private final int status;
        @JsonInclude(JsonInclude.Include.NON_NULL)
        private String message;
        @JsonInclude(JsonInclude.Include.NON_NULL)
        private T data;
        @JsonInclude(JsonInclude.Include.NON_NULL)
        private T error;

        public Payload(int status, String message) {
            this.status = status;
            this.message = message;
        }

        public Payload(int status, T error) {
            this.status = status;
            this.error = error;
        }

        public Payload(int status, String message, T data) {
            this.status = status;
            this.message = message;
            this.data = data;
        }

        public int getStatus() {
            return status;
        }

        public String getMessage() {
            return message;
        }

        public T getData() {
            return data;
        }

        public T getError() {
            return error;
        }

    }

}
