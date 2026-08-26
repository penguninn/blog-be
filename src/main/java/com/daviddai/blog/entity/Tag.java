package com.daviddai.blog.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "tags", schema = "public")
public class Tag extends AbstractEntity {

    @Column(name = "display_name", nullable = false, unique = true)
    private String displayName;

    @Column(name = "slug", nullable = false, unique = true)
    private String slug;
}
