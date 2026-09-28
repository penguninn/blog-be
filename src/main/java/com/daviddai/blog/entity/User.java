package com.daviddai.blog.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "app_users")
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

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false, unique = true)
    private String email;

    private String pendingEmail;

    @Builder.Default
    private boolean emailVerified = false;

    private String identitySyncStatus;

    @Builder.Default
    private boolean enabled = true;
}
