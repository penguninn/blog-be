package com.daviddai.blog.entity;

import com.daviddai.blog.enums.PostStatus;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OrderColumn;
import jakarta.persistence.Table;
import lombok.*;

import java.time.Instant;
import java.util.List;

@Entity
@Table(name = "posts")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Post extends AbstractEntity {
    
    private String title;

    @Column(nullable = false, unique = true)
    private String slug;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private PostStatus status = PostStatus.DRAFT;

    @Builder.Default
    private long views = 0;

    @Builder.Default
    private long likesCount = 0;

    private String excerpt;

    private Instant publishedAt;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "post_contents", joinColumns = @JoinColumn(name = "post_id"))
    @OrderColumn(name = "content_order")
    private List<PostContent> contents;
    
    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "post_asset_public_ids", joinColumns = @JoinColumn(name = "post_id"))
    @Column(name = "public_id")
    private List<String> assetPublicIds;

    private String userId;

    private String categoryId;

}
