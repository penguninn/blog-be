package com.daviddai.blog.services.impl;

import java.util.Optional;
import java.util.Set;

import com.daviddai.blog.exceptions.AuthServiceException;
import com.daviddai.blog.exceptions.JwtException;
import com.daviddai.blog.exceptions.RoleNotFoundException;
import com.daviddai.blog.exceptions.UserAlreadyExistException;
import com.daviddai.blog.mappers.UserMapper;
import com.daviddai.blog.model.dtos.request.UserLoginRequest;
import com.daviddai.blog.model.dtos.request.UserRegisterRequest;
import com.daviddai.blog.model.dtos.response.AuthResponse;
import com.daviddai.blog.model.entities.Role;
import com.daviddai.blog.model.entities.Token;
import com.daviddai.blog.model.entities.User;
import com.daviddai.blog.model.enums.ERole;
import com.daviddai.blog.model.enums.EToken;
import com.daviddai.blog.repositories.RoleRepository;
import com.daviddai.blog.repositories.TokenRepository;
import com.daviddai.blog.repositories.UserRepository;
import com.daviddai.blog.security.JwtService;
import com.daviddai.blog.security.UserDetailsImpl;
import com.daviddai.blog.services.AuthService;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final TokenRepository tokenRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Override
    @Transactional
    public AuthResponse register(UserRegisterRequest userRequest) {
        AuthResponse authResponse;
        log.info("AuthService::register execution started");
        if (isUserExists(userRequest.getUsername(), userRequest.getEmail())) {
            throw new UserAlreadyExistException("user already exists");
        }
        Role defaultRole = roleRepository.findByName(ERole.ROLE_USER)
                .orElseThrow(() -> new RoleNotFoundException("role not found"));
        try {
            User user = UserMapper.mapToEntity(userRequest);
            String encodedPassword = passwordEncoder.encode(userRequest.getPassword());
            user.setPassword(encodedPassword);
            user.getRoles().add(defaultRole);
            userRepository.save(user);

            UserDetailsImpl userDetails = UserMapper.mapToUserDetails(user);
            String accessToken = jwtService.generateToken(userDetails);
            String refreshToken = jwtService.generateRefreshToken(userDetails);

            saveToken(user, refreshToken);

            authResponse = AuthResponse.builder()
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .build();
        } catch (RuntimeException e) {
            log.error("Exception occurred while persisting user to database, Exception message: {}", e.getMessage());
            throw new AuthServiceException("create new account failed");
        }
        log.info("AuthService::register execution ended");
        return authResponse;
    }

    @Override
    @Transactional
    public AuthResponse authenticate(UserLoginRequest userRequest) {
        AuthResponse authResponse;
        log.info("AuthService::authenticate execution started");
        User user = userRepository.findByUsername(userRequest.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("username not found"));
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            userRequest.getUsername(),
                            userRequest.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            UserDetails userDetails = (UserDetailsImpl) authentication.getPrincipal();

            String accessToken = jwtService.generateToken(userDetails);
            String refreshToken = jwtService.generateRefreshToken(userDetails);

            revokeAllUserTokens(user);
            saveToken(user, refreshToken);

            authResponse = AuthResponse.builder()
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .build();

        } catch (BadCredentialsException e) {
            log.error("Bad credentials: {}", e.getMessage());
            throw new AuthServiceException("Invalid username or password");
        } catch (RuntimeException e) {
            log.error("Exception occurred while authenticate, Exception message: {}", e.getMessage());
            throw new AuthServiceException("authenticate failed");
        }
        log.info("AuthService::authenticate execution ended");
        return authResponse;

    }

    private boolean isUserExists(String username, String email) {
        return userRepository.existsByUsername(username) || userRepository.existsByEmail(email);
    }

    private void saveToken(User user, String token) {
        Token newToken = Token.builder()
                .refreshToken(token)
                .tokenType(EToken.BEARER)
                .expried(false)
                .revoked(false)
                .build();
        tokenRepository.save(newToken);
        user.getTokens().add(newToken);
        userRepository.save(user);
    }

    private void revokeAllUserTokens(User user) {
        Set<Token> userTokens = user.getTokens();
        userTokens.stream()
                .filter(t -> !t.isRevoked() && !t.isExpried())
                .forEach(t -> {
                    t.setRevoked(true);
                    t.setExpried(true);
                });
        if (!userTokens.isEmpty()) {
            tokenRepository.saveAll(userTokens);
            userRepository.save(user);
        }
    }

    @Override
    @Transactional
    public AuthResponse refreshToken(String refreshToken) {
        log.info("AuthService::refreshToken execution started");
        String username = jwtService.extractUsername(refreshToken);
        if (username == null) {
            throw new AuthServiceException("Invalid refresh token");
        }
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("username not found"));
        Optional<Token> token = tokenRepository.findByRefreshToken(refreshToken);
        if (token.isEmpty() || token.get().isRevoked() || token.get().isExpried()) {
            throw new JwtException("Refresh token is expired or revoked");
        }
        if (!jwtService.isTokenValid(refreshToken, UserMapper.mapToUserDetails(user))) {
            throw new JwtException("Invalid refresh token");
        }

        UserDetailsImpl userDetails = UserMapper.mapToUserDetails(user);
        String accessToken = jwtService.generateToken(userDetails);
        log.info("AuthService::refreshToken execution ended");
        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }
}
