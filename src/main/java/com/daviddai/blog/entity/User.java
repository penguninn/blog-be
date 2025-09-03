package com.daviddai.blog.entity;

import lombok.*;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.time.LocalDate;

@Document(collection = "users")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User extends AbstractEntity {

    private String userId;

    private String displayName;

    private boolean gender;

    private LocalDate dob;

    private String avatarUrl;

    private String bio;

    @Indexed(unique = true)
    private String username;

    @Indexed(unique = true)
    private String email;

    private String pendingEmail;

    @Builder.Default
    private boolean emailVerified = false;

    private String identitySyncStatus;

    @Builder.Default
    private boolean enabled = true;
}
