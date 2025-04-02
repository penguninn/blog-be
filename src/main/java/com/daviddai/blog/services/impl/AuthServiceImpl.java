package com.daviddai.blog.services.impl;

import com.daviddai.blog.exceptions.AuthServiceException;
import com.daviddai.blog.exceptions.RoleNotFoundException;
import com.daviddai.blog.exceptions.UserAlreadyExistException;
import com.daviddai.blog.mappers.UserMapper;
import com.daviddai.blog.model.dtos.requset.UserLoginRequest;
import com.daviddai.blog.model.dtos.requset.UserRegisterRequest;
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
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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
            String encodedPassword = passwordEncoder.encode(user.getPassword());
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
        } catch (Exception e) {
            log.error("Exception occurred while persisting user to database, Exception message: {}", e.getMessage());
            throw new AuthServiceException("create new account failed");
        }
        log.info("AuthService::register execution ended");
        return authResponse;
    }

    @Override
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

            saveToken(user, refreshToken);

            authResponse = AuthResponse.builder()
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .build();

        } catch (Exception e) {
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
}
