package com.daviddai.blog.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;

import com.daviddai.blog.authentication.JwtService;
import com.daviddai.blog.dto.request.SigninReq;
import com.daviddai.blog.dto.request.SignupReq;
import com.daviddai.blog.dto.response.SigninRes;
import com.daviddai.blog.exception.AppException;
import com.daviddai.blog.exception.ErrorCode;
import com.daviddai.blog.repository.UserRepository;

import io.swagger.v3.oas.annotations.servers.Server;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Server
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public SigninRes signin(SigninReq req) {
        Authentication authentication;

        try {
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            req.email(), req.password()));
        } catch (AuthenticationException e) {
            throw new AppException(ErrorCode.INVALID_CREDENTIALS);
        }
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return SigninRes.builder()
                .accessToken(jwtService.generateAccessToken(userDetails))
                .refreshToken(jwtService.generateRefreshToken(userDetails))
                .build();
    }

    public void signup(SignupReq req) {
        if (userRepository.findByEmail(req.email()) != null) {
            throw new AppException(ErrorCode.EMAIL_ALREADY_EXISTS);
        }

    }
}
