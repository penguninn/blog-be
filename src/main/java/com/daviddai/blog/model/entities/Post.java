package com.daviddai.blog.model.entities;

import java.util.List;

import com.daviddai.blog.model.enums.PostStatus;

import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Document(collection = "posts")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Post extends AbstractEntity {
    
    private String title;

    private String slug;

    @DBRef
    private User user;

    @DBRef
    private Category category;

    @DBRef
    private List<Tag> tags;

    private PostStatus status;

    private long view;

    private List<PostContent> contents;
}
