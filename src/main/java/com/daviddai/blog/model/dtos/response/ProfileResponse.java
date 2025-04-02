package com.daviddai.blog.model.dtos.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ProfileResponse {

    private String id;

    private String name;

    private String username;

    private String email;
    
}
