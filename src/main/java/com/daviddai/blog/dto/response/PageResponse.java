package com.daviddai.blog.dto.response;

import java.io.Serializable;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PageResponse<T> implements Serializable {

    private int page;

    private int size;

    private int totalPages;

    private long totalElements;

    private T content;
}
