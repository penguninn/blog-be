package com.daviddai.blog.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostEngagementResponse {
    private String postId;
    private long views;
    private long likesCount;
    private boolean liked;
}