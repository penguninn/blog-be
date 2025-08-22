package com.daviddai.blog.model.dtos.response;

import com.fasterxml.jackson.annotation.JsonInclude;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

public class ResponseError<T> extends ResponseEntity<ResponseError.Payload<T>> {

    public ResponseError(HttpStatusCode status, String message) {
        super(new Payload<>(status.value(), message), HttpStatus.OK);
    }

    public ResponseError(HttpStatusCode status, String message, T error) {
        super(new Payload<>(status.value(), message, error), HttpStatus.OK);
    }

    public static class Payload<T> {

        private final int status;
        @JsonInclude(JsonInclude.Include.NON_NULL)
        private final String message;
        @JsonInclude(JsonInclude.Include.NON_NULL)
        private T error;

        public Payload(int status, String message) {
            this.status = status;
            this.message = message;
        }

        public Payload(int status, String message, T error) {
            this.status = status;
            this.message = message;
            this.error = error;
        }

        public T getError() {
            return error;
        }

        public String getMessage() {
            return message;
        }

        public int getStatus() {
            return status;
        }
    }

}
