package com.daviddai.blog.exception;

public class AssetUploadException extends RuntimeException {

    public AssetUploadException(String message) {
        super(message);
    }

    public AssetUploadException(String message, Throwable cause) {
        super(message, cause);
    }
}