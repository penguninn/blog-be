package com.daviddai.blog.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CategoryRequest {
    
    @NotBlank(message = "Category name is required")
    @Size(min = 2, max = 50, message = "Category name must be between 2 and 50 characters")
    @Pattern(regexp = "^[a-zA-Z0-9\\s\\-_&]+$", message = "Category name can only contain letters, numbers, spaces, hyphens, underscores and ampersands")
    private String name;

    @Size(max = 200, message = "Description must not exceed 200 characters")
    private String description;
}