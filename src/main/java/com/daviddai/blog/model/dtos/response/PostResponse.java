package com.daviddai.blog.model.dtos.response;

import java.util.List;

import com.daviddai.blog.model.entities.Category;
import com.daviddai.blog.model.entities.PostContent;

import com.daviddai.blog.model.enums.PostStatus;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PostResponse {

    private String id;

    private String title;

    private String slug;

    private String author;

    private PostStatus status;

    private CategoryResponse category;

    private List<TagResponse> tags;

    private List<PostContent> contents;
}
