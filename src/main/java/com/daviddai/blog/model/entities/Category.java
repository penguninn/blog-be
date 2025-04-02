package com.daviddai.blog.model.entities;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.Setter;

@Document
@Getter
@Setter
public class Category extends AbstractEntity {

    private String name;
    
}
