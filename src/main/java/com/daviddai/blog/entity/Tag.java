package com.daviddai.blog.entity;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "tags")
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Tag extends AbstractEntity {

    private String name;
    private String description;
    private String color;
}
