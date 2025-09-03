package com.daviddai.blog.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Builder
public class UserResponse {
    private String id;
    private String userId;
    private String displayName;
    private boolean gender;
    private LocalDate dob;
    private String avatarUrl;
    private String bio;
    private String username;
    private String email;
    private String pendingEmail;
    private boolean emailVerified;
    private boolean enabled;
    private String identitySyncStatus;
    private Instant createdDate;
    private Instant modifiedDate;
}
