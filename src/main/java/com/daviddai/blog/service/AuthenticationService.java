package com.daviddai.blog.service;

import java.awt.datatransfer.Clipboard;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.daviddai.blog.authentication.JwtService;
import com.daviddai.blog.dto.request.SigninReq;
import com.daviddai.blog.dto.request.SignupReq;
import com.daviddai.blog.dto.response.AuthResponse;
import com.daviddai.blog.entity.User;
import com.daviddai.blog.enums.AppCode;
import com.daviddai.blog.enums.TokenType;
import com.daviddai.blog.exception.AppException;
import com.daviddai.blog.repository.UserRepository;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;

    public AuthResponse signin(SigninReq req) {
        log.info("AuthenticationService - signin: start");
        Authentication authentication;
        try {
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            req.email(), req.password()));
        } catch (AuthenticationException e) {
            log.warn("Authentication failed for email={}", req.email());
            throw new AppException(AppCode.INVALID_CREDENTIALS);
        }
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return new AuthResponse(
                jwtService.generateAccessToken(userDetails),
                jwtService.generateRefreshToken(userDetails));
    }

    public void signup(SignupReq req) {
        log.info("AuthenticationService - signup: start");
        if (userRepository.existsByEmail(req.email())) {
            throw new AppException(AppCode.EMAIL_ALREADY_EXISTS);
        }
        User user = new User();
        user.setEmail(req.email());
        user.setDisplayName(req.displayName());
        user.setPasswordHash(passwordEncoder.encode(req.password()));
        try {
            userRepository.save(user);
        } catch (DataIntegrityViolationException e) {
            throw new AppException(AppCode.EMAIL_ALREADY_EXISTS);
        }
    }

    public AuthResponse refresh(String refreshToken) {
        log.info("AuthenticationService - refresh: start");
        if (refreshToken == null || refreshToken.isBlank()) {
            throw new AppException(AppCode.INVALID_TOKEN);
        }
        Claims claims = jwtService.parseClaims(refreshToken);
        String type = claims.get("type", String.class);
        if (!TokenType.REFRESH.name().equals(type)) {
            throw new AppException(AppCode.INVALID_TOKEN);
        }
        String email = claims.getSubject();
        UserDetails userDetails = userDetailsService.loadUserByUsername(email);
        String newAccessToken = jwtService.generateAccessToken(userDetails);
        return new AuthResponse(newAccessToken, refreshToken);
    }

}
