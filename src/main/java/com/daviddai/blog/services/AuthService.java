package com.daviddai.blog.services;

import com.daviddai.blog.model.dtos.requset.UserLoginRequest;
import com.daviddai.blog.model.dtos.requset.UserRegisterRequest;
import com.daviddai.blog.model.dtos.response.AuthResponse;

public interface AuthService {

    AuthResponse register(UserRegisterRequest userRequest);

    AuthResponse authenticate(UserLoginRequest userRequest);

}
