package com.daviddai.blog.dto.request;

import lombok.*;

import java.time.Instant;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostPublishRequest {
    
    private boolean immediate;
    
    private Instant scheduledFor;
}