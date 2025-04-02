package com.daviddai.blog.mappers;

import com.daviddai.blog.model.dtos.response.CategoryResponse;
import com.daviddai.blog.model.entities.Category;

public class CategoryMapper {

    public static CategoryResponse mapToDto(Category category) {
        return CategoryResponse.builder()
                .id(category.getId())
                .name(category.getName())
                .build();
    }
}
