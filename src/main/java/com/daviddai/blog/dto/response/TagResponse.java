package com.daviddai.blog.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TagResponse {

    private String id;

    private String name;
}
