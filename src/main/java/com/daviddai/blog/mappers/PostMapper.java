package com.daviddai.blog.mappers;

import com.daviddai.blog.model.dtos.requset.PostRequest;
import com.daviddai.blog.model.dtos.response.PostResponse;
import com.daviddai.blog.model.entities.Post;

public class PostMapper {

    public static PostResponse mapToDto(Post post) {
        return PostResponse.builder()
                .id(post.getId())
                .title(post.getTitle())
                .slug(post.getSlug())
                .author("admin")
                .status(post.getStatus())
                .category(CategoryMapper.mapToDto(post.getCategory()))
                .tags(post.getTags().stream().map(TagMapper::mapToDto).toList())
                .contents(post.getContents())
                .build();
    }

    public static Post mapToEntity(PostRequest postRequest) {
        return Post.builder()
                .title(postRequest.getTitle())
                .slug(postRequest.getSlug())
                .status(postRequest.getStatus())
                .category(postRequest.getCategory())
                .tags(postRequest.getTags())
                .contents(postRequest.getContents())
                .build();
    }

}
