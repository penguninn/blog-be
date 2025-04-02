package com.daviddai.blog.model.dtos.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AuthResponse {

    private String accessToken;

    private String refreshToken;

}
