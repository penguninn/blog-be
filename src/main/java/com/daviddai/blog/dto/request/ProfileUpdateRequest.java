package com.daviddai.blog.dto.request;

import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.Instant;
import java.time.LocalDate;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProfileUpdateRequest {

    @Size(max = 100, message = "Display name must not exceed 100 characters")
    private String displayName;

    private boolean gender;

    private LocalDate dob;

    @Size(max = 255, message = "Avatar URL must not exceed 255 characters")
    private String avatarUrl;

    @Size(max = 500, message = "Bio must not exceed 500 characters")
    private String bio;
}
