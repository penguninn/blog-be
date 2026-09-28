package com.daviddai.blog.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.time.Instant;

@Data
@Entity
@Table(name = "assets")
public class Asset {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    
    @Column(nullable = false, unique = true)
    private String publicId;
    
    private String url;
    private Integer width;
    private Integer height;
    private Integer refCount = 0;
    private Long bytes;
    private String format;
    private String createdBy;
    private Instant createdAt;
    
    private Instant scheduledDeleteAt;
}
