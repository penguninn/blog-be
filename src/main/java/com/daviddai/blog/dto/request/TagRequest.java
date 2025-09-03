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
public class TagRequest {
    
    @NotBlank(message = "Tag name is required")
    @Size(min = 2, max = 30, message = "Tag name must be between 2 and 30 characters")
    @Pattern(regexp = "^[a-zA-Z0-9\\-_#+.]+$", message = "Tag name can only contain letters, numbers, hyphens, underscores, hash, plus and dots")
    private String name;

    @Size(max = 100, message = "Description must not exceed 100 characters")
    private String description;

    @Pattern(regexp = "^#[0-9A-Fa-f]{6}$", message = "Color must be a valid hex color code")
    private String color;
}