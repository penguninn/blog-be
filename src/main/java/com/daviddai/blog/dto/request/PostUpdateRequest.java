package com.daviddai.blog.dto.request;

import com.daviddai.blog.entity.PostContent;
import com.daviddai.blog.enums.PostStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class PostUpdateRequest {

    @NotBlank(message = "Title is required")
    @Size(min = 3, max = 200, message = "Title must be between 3 and 200 characters")
    private String title;

    @NotNull(message = "Status is required")
    private PostStatus status;

    @NotBlank(message = "Category ID is required")
    private String categoryId;


    private List<PostContent> contents;
}