package com.daviddai.blog.mappers;

import com.daviddai.blog.model.dtos.response.CategoryResponse;
import com.daviddai.blog.model.dtos.response.TagResponse;
import com.daviddai.blog.model.entities.Category;
import com.daviddai.blog.model.entities.Tag;

public class TagMapper {

    public static TagResponse mapToDto(Tag tag) {
        return TagResponse.builder()
                .id(tag.getId())
                .name(tag.getName())
                .build();
    }
}
