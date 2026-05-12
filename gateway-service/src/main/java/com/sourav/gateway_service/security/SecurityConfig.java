package com.sourav.gateway_service.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.config.web.server.ServerHttpSecurity;

import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(
            ServerHttpSecurity http
    ) {

        return http

                // Disable CSRF
                .csrf(ServerHttpSecurity.CsrfSpec::disable)

                // Disable form login
                .formLogin(ServerHttpSecurity.FormLoginSpec::disable)

                // Disable basic auth
                .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)

                // Allow all requests
                .authorizeExchange(exchange ->
                        exchange.anyExchange().permitAll()
                )

                .build();
    }
}