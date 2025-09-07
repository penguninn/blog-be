package com.daviddai.blog.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.index.Indexed;

import java.time.Instant;

@Data
@Document(collection = "assets")
public class Asset {
    
    @Id
    private String id;
    
    @Indexed(unique = true)
    private String publicId;
    
    private String url;
    private Integer width;
    private Integer height;
    private Integer refCount = 0;
    private Long bytes;
    private String format;
    private String createdBy;
    private Instant createdAt;
    
    @Indexed
    private Instant scheduledDeleteAt;
}