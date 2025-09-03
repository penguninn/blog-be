package com.daviddai.blog.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;
import java.util.List;

@Getter
@Builder
public class PageResponse<T> implements Serializable {

    private int page;
    private int size;
    private int totalPages;
    private long totalElements;
    private List<T> contents;
}
