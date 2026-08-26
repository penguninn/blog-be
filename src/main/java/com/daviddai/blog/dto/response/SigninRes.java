package com.daviddai.blog.dto.response;

import java.io.Serializable;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class SigninRes implements Serializable{

    private String accessToken;

    private String refreshToken;
}
