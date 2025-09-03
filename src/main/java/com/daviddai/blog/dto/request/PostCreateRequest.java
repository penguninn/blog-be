package com.daviddai.blog.dto.request;

import com.daviddai.blog.entity.PostContent;
import com.daviddai.blog.enums.PostStatus;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostCreateRequest {

    @NotBlank(message = "Title is required")
    @Size(min = 3, max = 200, message = "Title must be between 3 and 200 characters")
    private String title;

    private String slug;

    @Size(max = 500, message = "Excerpt must not exceed 500 characters")
    private String excerpt;

    @NotNull(message = "Status is required")
    private PostStatus status;

    @NotBlank(message = "Category ID is required")
    private String categoryId;

    @Size(min = 1, max = 10, message = "Must have 1-10 tags")
    private List<String> tagIds;

    @Valid
    @Size(min = 1, message = "Post must have at least one content section")
    private List<PostContent> contents;
}
