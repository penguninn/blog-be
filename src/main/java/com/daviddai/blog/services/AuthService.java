package com.daviddai.blog.services;

import com.daviddai.blog.model.dtos.request.UserLoginRequest;
import com.daviddai.blog.model.dtos.request.UserRegisterRequest;
import com.daviddai.blog.model.dtos.response.AuthResponse;

public interface AuthService {

    AuthResponse register(UserRegisterRequest userRequest);

    AuthResponse authenticate(UserLoginRequest userRequest);

    AuthResponse refreshToken(String refreshToken);

}
