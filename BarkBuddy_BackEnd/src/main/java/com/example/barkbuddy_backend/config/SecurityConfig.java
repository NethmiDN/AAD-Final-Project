package com.example.barkbuddy_backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfigurationSource;

import com.example.barkbuddy_backend.service.Impl.GoogleOAuth2Service;
import com.example.barkbuddy_backend.util.JWTAuthFilter;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JWTAuthFilter jwtAuthFilter;
    private final CorsConfigurationSource corsConfigurationSource;
    private final GoogleOAuth2Service googleOAuth2Service;
    private final AuthenticationProvider authenticationProvider;
    

    // Chain 1: OAuth2 endpoints (session-based)
    @Bean
    @Order(1)
    public SecurityFilterChain oauth2SecurityFilterChain(HttpSecurity http) throws Exception {
    http
        .securityMatcher("/oauth2/**", "/login/oauth2/**", "/oauth2/success")
        .csrf(csrf -> csrf.disable())
        .cors(cors -> cors.configurationSource(corsConfigurationSource))
        .authorizeHttpRequests(auth -> auth
            .requestMatchers("/oauth2/**", "/login/oauth2/**", "/oauth2/success").permitAll()
            .anyRequest().authenticated()
        )
        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))
        .oauth2Login(oauth2 -> oauth2
            .userInfoEndpoint(userInfo -> userInfo.userService(googleOAuth2Service))
            .defaultSuccessUrl("/oauth2/success", true)
        );

    return http.build();
    }

    // Chain 2: API and application endpoints (JWT/stateless)
    @Bean
    @Order(2)
    public SecurityFilterChain apiSecurityFilterChain(HttpSecurity http) throws Exception {
    http
        .securityMatcher("/**")
        .csrf(csrf -> csrf.disable())
        .cors(cors -> cors.configurationSource(corsConfigurationSource))
        .authorizeHttpRequests(auth -> auth
            // Explicitly permit auth endpoints first
            .requestMatchers(HttpMethod.POST, "/auth/barkbuddy/login", "/auth/barkbuddy/register").permitAll()
            .requestMatchers("/auth/barkbuddy/**").permitAll()
            // Allow Spring Boot's error page to avoid 403 masking real errors
            .requestMatchers("/error").permitAll()
            .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
            .requestMatchers("/api/chat/**").permitAll()
            .requestMatchers("/api/user-chat/**").authenticated()
            .requestMatchers("/api/dogs/all").permitAll()
            .requestMatchers("/api/dogs/*/image").permitAll()
            .requestMatchers("/api/dogs/**").authenticated()
            .requestMatchers("/api/lostdogs/**").permitAll()
            .requestMatchers("/api/adoption/**").permitAll()
            .requestMatchers("/api/listings/**").permitAll()
            .requestMatchers("/api/sightings/**").permitAll()
            .requestMatchers("/api/users/**").permitAll()
            .requestMatchers("/api/admin/**").permitAll()
            .requestMatchers("/api/orders/**").permitAll()
            .anyRequest().authenticated()
        )
        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
    .authenticationProvider(authenticationProvider)
        .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

    return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationProvider authenticationProvider) {
        return new ProviderManager(authenticationProvider);
    }

    // If another bean needs DefaultOAuth2UserService, we can expose it:
    // @Bean
    // public OAuth2UserService<OAuth2UserRequest, OAuth2User> oauth2UserService() {
    //     return new DefaultOAuth2UserService();
    // }

}
