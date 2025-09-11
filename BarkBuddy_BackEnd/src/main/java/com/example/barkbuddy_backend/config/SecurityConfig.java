package com.example.barkbuddy_backend.config;

import com.example.barkbuddy_backend.service.Impl.GoogleOAuth2Service;
import com.example.barkbuddy_backend.util.JWTAuthFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfigurationSource;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JWTAuthFilter jwtAuthFilter;
    private final CorsConfigurationSource corsConfigurationSource;
    private final GoogleOAuth2Service googleOAuth2Service;
    

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configurationSource(corsConfigurationSource))
        .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/barkbuddy/**").permitAll()
            .requestMatchers("/oauth2/**", "/login/oauth2/**").permitAll()
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
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
        .oauth2Login(oauth2 -> oauth2
            .userInfoEndpoint(userInfo -> userInfo.userService(googleOAuth2Service))
                        .defaultSuccessUrl("/oauth2/success", true) // always redirect here after Google login
                );

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    // If another bean needs DefaultOAuth2UserService, we can expose it:
    // @Bean
    // public OAuth2UserService<OAuth2UserRequest, OAuth2User> oauth2UserService() {
    //     return new DefaultOAuth2UserService();
    // }

}
