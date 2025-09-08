package com.daviddai.blog.mapper;

import com.daviddai.blog.dto.response.CategoryResponse;
import com.daviddai.blog.dto.response.PostResponse;
import com.daviddai.blog.entity.Category;
import com.daviddai.blog.entity.Post;
import com.daviddai.blog.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {CategoryMapper.class})
public interface PostMapper {

    @Mapping(target = "id", source = "post.id")
    @Mapping(target = "userId", source = "post.userId")
    @Mapping(target = "authorName", source = "user.displayName")
    @Mapping(target = "category", source = "category")
    @Mapping(target = "categoryId", source = "post.categoryId")
    @Mapping(target = "createdDate", source = "post.createdDate")
    @Mapping(target = "modifiedDate", source = "post.modifiedDate")
    PostResponse mapToDto(Post post, User user, Category category);

    @Mapping(target = "authorName", constant = "")
    @Mapping(target = "category", ignore = true)
    PostResponse mapToDtoWithoutRelations(Post post);
}
