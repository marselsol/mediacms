package com.marselgaisin.mediacms.auth.service;

import java.util.Locale;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.marselgaisin.mediacms.auth.dto.AuthResponse;
import com.marselgaisin.mediacms.auth.dto.LoginRequest;
import com.marselgaisin.mediacms.auth.dto.RegisterRequest;
import com.marselgaisin.mediacms.auth.model.AppUser;
import com.marselgaisin.mediacms.auth.model.Role;
import com.marselgaisin.mediacms.auth.security.JwtService;
import com.marselgaisin.mediacms.common.exception.BadRequestException;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private static final String BEARER_PREFIX = "Bearer ";

    public void register(RegisterRequest request) {
        String normalizedUsername = request.getUsername().trim().toLowerCase(Locale.ROOT);
        if (userService.existsByUsername(normalizedUsername)) {
            throw new BadRequestException("Username already exists");
        }

        AppUser user = new AppUser();
        user.setUsername(normalizedUsername);
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.EDITOR);
        userService.save(user);
    }

    public AuthResponse login(LoginRequest request) {
        AppUser user = userService.findByUsername(request.getUsername().trim().toLowerCase(Locale.ROOT))
                .orElseThrow(() -> new BadRequestException("Invalid username or password"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            throw new BadRequestException("Invalid username or password");
        }

        String token = jwtService.generateToken(user.getUsername());
        return new AuthResponse(BEARER_PREFIX + token);
    }
}
