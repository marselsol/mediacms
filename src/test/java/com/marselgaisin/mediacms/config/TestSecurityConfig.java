package com.marselgaisin.mediacms.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import com.marselgaisin.mediacms.auth.security.JwtAuthFilter;
import com.marselgaisin.mediacms.auth.security.JwtService;

@TestConfiguration
public class TestSecurityConfig {

    @Bean
    public JwtService jwtService(@Value("${security.jwt.secret}") String secret) {
        return new JwtService(secret);
    }

    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails editor = User.withUsername("editor")
                .password("test")
                .roles("EDITOR")
                .build();
        return new InMemoryUserDetailsManager(editor);
    }

    @Bean
    public JwtAuthFilter jwtAuthFilter(JwtService jwtService, UserDetailsService userDetailsService) {
        return new JwtAuthFilter(jwtService, userDetailsService);
    }
}
