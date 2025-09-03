package com.daviddai.blog.dto.request;

import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostSearchRequest {

    @Size(min = 2, max = 100, message = "Search query must be between 2 and 100 characters")
    private String query;

    private List<String> categoryIds;

    private List<String> tagIds;

    private String authorId;

    @Builder.Default
    private int page = 0;

    @Builder.Default
    private int size = 10;

    @Builder.Default
    private String sortBy = "relevance";

    @Builder.Default
    private String sortDirection = "desc";
}