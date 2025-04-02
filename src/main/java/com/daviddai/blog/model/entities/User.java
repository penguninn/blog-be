package com.daviddai.blog.model.entities;

import java.util.HashSet;
import java.util.Set;

import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Document(collection = "users")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User extends AbstractEntity {

    private String name;

    private String username;

    private String email;

    @ToString.Exclude
    private String password;

    @DBRef
    @Builder.Default
    private Set<Token> tokens = new HashSet<>();

    @DBRef
    @Builder.Default
    private Set<Role> roles = new HashSet<>();

    @DBRef
    @Builder.Default
    private Set<Post> posts = new HashSet<>();

}
