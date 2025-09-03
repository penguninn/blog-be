package com.daviddai.blog.entity;

import com.daviddai.blog.enums.PostStatus;
import lombok.*;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.List;

@Document(collection = "posts")
@CompoundIndex(def = "{'status': 1, 'createdAt': -1}")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Post extends AbstractEntity {
    
    private String title;

    @Indexed(unique = true)
    private String slug;

    @Indexed
    @Builder.Default
    private PostStatus status = PostStatus.DRAFT;

    @Builder.Default
    private long views = 0;

    @Builder.Default
    private long likesCount = 0;

    private String excerpt;

    private Instant publishedAt;

    private List<PostContent> contents;

    @Indexed
    private String userId;

    @Indexed
    private String categoryId;

    @Indexed
    private List<String> tagIds;
}
