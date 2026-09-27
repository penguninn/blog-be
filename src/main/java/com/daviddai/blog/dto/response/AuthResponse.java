package com.daviddai.blog.dto.response;

public record AuthResponse(

        String accessToken,

        String refreshToken) {
}
