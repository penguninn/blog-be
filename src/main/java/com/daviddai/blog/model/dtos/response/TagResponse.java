package com.daviddai.blog.model.dtos.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Builder
public class TagResponse {

    private String id;

    private String name;
}
