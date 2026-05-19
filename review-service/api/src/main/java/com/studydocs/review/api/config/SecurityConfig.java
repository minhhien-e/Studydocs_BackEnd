package com.studydocs.review.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(HttpMethod.GET, "/api/v1/reviews/{id}").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/v1/reviews/{id}/replies").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/v1/documents/{documentId}/reviews").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/v1/documents/{documentId}/statistics").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/v1/users/{userId}/reviews").permitAll()
                .anyRequest().authenticated()
            )
            .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()));
        return http.build();
    }
}
