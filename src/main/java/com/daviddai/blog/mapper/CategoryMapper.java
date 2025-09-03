package com.daviddai.blog.mapper;

import com.daviddai.blog.dto.response.CategoryResponse;
import com.daviddai.blog.entity.Category;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    CategoryResponse mapToDto(Category category);
}
