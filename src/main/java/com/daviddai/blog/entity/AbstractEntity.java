package com.daviddai.blog.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.*;

import java.time.Instant;
import java.time.LocalDateTime;

@Getter
@Setter
public abstract class AbstractEntity {

    @Id
    private String id;

    @CreatedDate
    private Instant createdDate;

    @LastModifiedDate
    private Instant modifiedDate;

    @CreatedBy
    private String createdBy;

    @LastModifiedBy
    private String lastModifiedBy;
}
