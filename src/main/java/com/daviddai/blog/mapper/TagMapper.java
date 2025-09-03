package com.daviddai.blog.mapper;

import com.daviddai.blog.dto.response.TagResponse;
import com.daviddai.blog.entity.Tag;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TagMapper {

    TagResponse mapToDto(Tag tag);
}
