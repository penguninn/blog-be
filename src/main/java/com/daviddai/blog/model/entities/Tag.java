package com.daviddai.blog.model.entities;

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
}
