package com.daviddai.blog.controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

import com.daviddai.blog.model.dtos.request.UserLoginRequest;
import com.daviddai.blog.model.dtos.request.UserRegisterRequest;
import com.daviddai.blog.model.dtos.response.AuthResponse;
import com.daviddai.blog.model.dtos.response.ResponseSuccess;
import com.daviddai.blog.services.AuthService;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/auth")
@Slf4j
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseSuccess<?> regiter(@Valid @RequestBody UserRegisterRequest userRequest) {
        log.info("AuthController::regiter execution started");
        AuthResponse authResponse = authService.register(userRequest);
        log.info("AuthController::register execution ended");
        return new ResponseSuccess<>(HttpStatus.CREATED, "Create new account successfully", authResponse);
    }

    @PostMapping("/login")
    public ResponseSuccess<?> authenticate(@Valid @RequestBody UserLoginRequest userRequest) {
        log.info("AuthController::authenticate execution started");
        AuthResponse authResponse = authService.authenticate(userRequest);
        log.info("AuthController::authenticate execution ended");
        return new ResponseSuccess<>(HttpStatus.OK, "Authenticate successfully", authResponse);
    }

    @PostMapping("/refresh-token")
    public ResponseSuccess<?> refreshToken(HttpServletRequest request) {
        log.info("AuthController::refreshToken execution started");
        final String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            log.debug("AuthController::refreshToken failed to get refresh token from header");
            log.info("AuthController::refreshToken execution ended");
            return new ResponseSuccess<>(HttpStatus.UNAUTHORIZED, "Invalid token");
        }
        final String refreshToken = authHeader.substring(7);
        AuthResponse authResponse = authService.refreshToken(refreshToken);
        log.info("AuthController::refreshToken execution ended");
        return new ResponseSuccess<>(HttpStatus.OK, "Refresh token successfully", authResponse);
    }

}
