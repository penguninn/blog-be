package com.daviddai.blog.dto.response;

import com.daviddai.blog.entity.PostContent;
import com.daviddai.blog.enums.PostStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.Instant;
import java.util.List;

@Getter
@Builder
public class PostResponse {

    private String id;
    private String title;
    private String slug;
    private String userId;
    private String authorName;
    private PostStatus status;
    private String excerpt;
    private long views;
    private long likesCount;
    private Instant publishedAt;
    private String categoryId;
    private CategoryResponse category;
    private List<PostContent> contents;
    private Instant createdDate;
    private Instant modifiedDate;
}
