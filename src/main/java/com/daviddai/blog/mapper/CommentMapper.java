package com.daviddai.blog.mapper;

import com.daviddai.blog.dto.response.CommentResponse;
import com.daviddai.blog.entity.Comment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CommentMapper {

    @Mapping(target = "authorName", source = "author.displayName")
    @Mapping(target = "authorId", source = "author.id")
    @Mapping(target = "postId", source = "post.id")
    @Mapping(target = "parentCommentId", source = "parentComment.id")
    @Mapping(target = "replies", ignore = true)
    CommentResponse mapToDto(Comment comment);
}