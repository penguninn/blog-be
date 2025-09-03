package com.daviddai.blog.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.Instant;
import java.util.List;

@Getter
@Builder
public class CommentResponse {
    private String id;
    private String content;
    private String authorName;
    private String authorId;
    private String postId;
    private String parentCommentId;
    private int likesCount;
    private List<CommentResponse> replies;
    private Instant createdDate;
    private Instant modifiedDate;
}