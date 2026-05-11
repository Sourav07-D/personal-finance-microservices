package com.sourav.category_service.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    private static final Logger log =
            LoggerFactory.getLogger(JwtAuthFilter.class);

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        final String authHeader =
                request.getHeader("Authorization");

        // ============================
        // CHECK AUTH HEADER
        // ============================

        if (authHeader == null ||
                !authHeader.startsWith("Bearer ")) {

            filterChain.doFilter(request, response);
            return;
        }

        // ============================
        // EXTRACT TOKEN
        // ============================

        String token = authHeader.substring(7);

        try {

            // ============================
            // VALIDATE TOKEN
            // ============================

            if (jwtService.isTokenValid(token)
                    && SecurityContextHolder
                    .getContext()
                    .getAuthentication() == null) {

                // ============================
                // EXTRACT USER INFO
                // ============================

                String email =
                        jwtService.extractEmail(token);

                String role =
                        jwtService.extractRole(token);

                // ============================
                // CREATE AUTH OBJECT
                // ============================

                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(
                                email,
                                null,
                                List.of(
                                        new SimpleGrantedAuthority(role)
                                )
                        );

                authToken.setDetails(
                        new WebAuthenticationDetailsSource()
                                .buildDetails(request)
                );

                // ============================
                // SET SECURITY CONTEXT
                // ============================

                SecurityContextHolder
                        .getContext()
                        .setAuthentication(authToken);

                log.info("JWT authentication successful for user={}", email);
            }

        } catch (Exception ex) {

            // ============================
            // HANDLE INVALID TOKEN
            // ============================

            log.error("JWT validation failed: {}", ex.getMessage());

            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

            response.setContentType("application/json");

            response.getWriter().write("""
                    {
                      "status": 401,
                      "error": "UNAUTHORIZED",
                      "message": "Invalid or expired token"
                    }
                    """);

            return;
        }

        filterChain.doFilter(request, response);
    }
}