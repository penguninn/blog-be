package com.daviddai.blog.model.dtos.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.bind.annotation.GetMapping;

@Getter
@Builder
public class CategoryResponse {

    private String id;

    private String name;
}
