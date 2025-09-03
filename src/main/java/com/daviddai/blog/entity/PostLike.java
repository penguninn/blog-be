package com.daviddai.blog.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "post_likes")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@CompoundIndex(def = "{'post.id': 1, 'user.id': 1}", unique = true)
public class PostLike extends AbstractEntity {

    @DBRef(lazy = true)
    private Post post;

    @DBRef(lazy = true)
    private User user;
}