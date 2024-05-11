package com.simpleblogapi.simpleblogapiv2.configurations;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final CustomJwtDecoder customJwtDecoder;
    private final String[] PUBLIC_POST_ENDPOINTS = {
            "/api/v2/auth/login",
            "/api/v2/auth/register",
            "/api/v2/auth/introspect",
    };

    private final String[] PUBLIC_GET_ENDPOINTS = {
            "/api/v2/categories",
            "/api/v2/posts",
            "/api/v2/posts/status/**",

            // Swagger
            "/simple-blog-api-v2/api-docs",
            "/simple-blog-api-v2/api-docs/**",
            "/simple-blog-api-v2/swagger-resources",
            "/simple-blog-api-v2/swagger-resources/**",
            "/simple-blog-api-v2/configuration/ui",
            "/simple-blog-api-v2/configuration/security",
            "/simple-blog-api-v2/swagger-ui/**",
            "/simple-blog-api-v2/swagger-ui.html",
            "/simple-blog-api-v2/swagger-ui/index.html",
    };

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .authorizeHttpRequests(request ->
                        request
                                .requestMatchers(HttpMethod.POST, PUBLIC_POST_ENDPOINTS).permitAll()
                                .requestMatchers(HttpMethod.GET, PUBLIC_GET_ENDPOINTS).permitAll()
                                .anyRequest().authenticated());
        httpSecurity.oauth2ResourceServer(oAuth2 ->
                oAuth2.jwt(jwtConfigurer ->
                                jwtConfigurer.decoder(customJwtDecoder)
                                        .jwtAuthenticationConverter(jwtAuthenticationConverter()))
                        .authenticationEntryPoint(new JwtAuthenticationEntryPoint())
        );
        httpSecurity.csrf(AbstractHttpConfigurer::disable);
        return httpSecurity.build();
    }

    @Bean
    JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        jwtGrantedAuthoritiesConverter.setAuthorityPrefix("ROLE_");
        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);
        return jwtAuthenticationConverter;
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


}
