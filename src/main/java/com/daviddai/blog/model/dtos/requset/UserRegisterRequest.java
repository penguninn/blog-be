package com.daviddai.blog.model.dtos.requset;

import java.io.Serializable;

import jakarta.validation.constraints.NotBlank;

import com.daviddai.blog.utils.MatchesPassword;
import com.daviddai.blog.utils.ValidEmail;
import com.daviddai.blog.utils.ValidPassword;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@MatchesPassword
public class UserRegisterRequest implements Serializable {

    @NotBlank(message = "require name")
    private String name;

    @NotBlank(message = "require username")
    private String username;

    @ValidEmail
    private String email;

    @ValidPassword
    private String password;
    private String confirmPassword;

}
