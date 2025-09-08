package com.daviddai.blog.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CategoryResponse {

    private String id;

    private String name;

    private String description;
}
