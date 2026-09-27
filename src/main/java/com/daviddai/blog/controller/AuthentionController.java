package com.daviddai.blog.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.daviddai.blog.dto.request.RefreshReq;
import com.daviddai.blog.dto.request.SigninReq;
import com.daviddai.blog.dto.request.SignupReq;
import com.daviddai.blog.dto.response.ApiResponse;
import com.daviddai.blog.dto.response.AuthResponse;
import com.daviddai.blog.enums.AppCode;
import com.daviddai.blog.service.AuthenticationService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthentionController {

    private final AuthenticationService authenticationService;

    @PostMapping("/signin")
    public ApiResponse<?> signin(@Valid @RequestBody SigninReq req) {
        AuthResponse response = authenticationService.signin(req);
        return ApiResponse.ok(response);
    }

    @PostMapping("/signup")
    public ApiResponse<?> signup(@Valid @RequestBody SignupReq req) {
        authenticationService.signup(req);
        return ApiResponse.ok(AppCode.CREATED, null);
    }

    @PostMapping("/refresh")
    public ApiResponse<?> refresh(@Valid @RequestBody RefreshReq req) {
        AuthResponse response = authenticationService.refresh(req.refreshToken());
        return ApiResponse.ok(response);
    }

}
