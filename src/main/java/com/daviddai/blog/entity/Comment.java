package com.daviddai.blog.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document(collection = "comments")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class Comment extends AbstractEntity {

    private String content;

    @DBRef(lazy = true)
    private Post post;

    @DBRef(lazy = true)
    private User author;

    @DBRef(lazy = true)
    private Comment parentComment;

    private boolean isDeleted;

    private int likesCount;
}