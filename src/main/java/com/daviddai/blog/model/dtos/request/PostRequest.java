package com.daviddai.blog.model.dtos.request;

import java.util.ArrayList;
import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import com.daviddai.blog.model.entities.Category;
import com.daviddai.blog.model.entities.PostContent;
import com.daviddai.blog.model.entities.Tag;
import com.daviddai.blog.model.enums.PostStatus;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class PostRequest {

    @NotBlank(message = "require title")
    private String title;

    @NotBlank(message = "require slug")
    private String slug;

    @NotNull(message = "require status")
    private PostStatus status;

    @NotNull(message = "require category")
    private Category category;

    @NotNull(message = "require tag")
    private List<Tag> tags;

    @Builder.Default
    private List<PostContent> contents = new ArrayList<>();
}
