package com.daviddai.blog.model.dtos.request;

import java.io.Serializable;

import jakarta.validation.constraints.NotBlank;

import com.daviddai.blog.utils.ValidPassword;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserLoginRequest implements Serializable {

    @NotBlank(message = "require username")
    private String username;

    @ValidPassword
    @NotBlank(message = "require password")
    private String password;
}
