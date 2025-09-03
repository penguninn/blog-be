package com.daviddai.blog.enums;

public enum PostSortBy {
    CREATED_AT("createdDate"),
    UPDATED_AT("modifiedDate"),
    MODIFIED_AT("modifiedDate"),
    TITLE("title"),
    VIEWS("views");

    private final String fieldName;

    PostSortBy(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFieldName() {
        return fieldName;
    }
}