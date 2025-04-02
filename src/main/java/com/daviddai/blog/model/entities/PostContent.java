package com.daviddai.blog.model.entities;

import java.util.List;

import org.bson.Document;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class PostContent {

    private String type;

    private List<Document> content;
}
